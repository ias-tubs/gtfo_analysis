import argparse
from queue import Queue, Empty
import shutil
import os
from cpgqls_client_custom.client import CPGQLSClient
from utils.data_handler import *
from utils.result_handler import *
from utils.paths import *
from utils.log_handler import *
from utils.models import *
from utils.scala_parser import *
from datetime import datetime
import time
import threading
import traceback

# logging
result_log_path: str = None
result_yaml_path: str = None

#joern clients
clients = []
#if remote execution, joern server is not killed on timeout
remote = False

#threads
THREAD_COUNT = 8
#timeout (seconds) for a single program query - not applied to common queries
TIMEOUT = 1800


def main():
    starttime = datetime.now()
    args = parse_args()

    remote = args.remote
    # check if logging dir is passed as arg and if it exists already
    run_dir = ""
    if args.dir:
        run_dir = os.path.join(RESULT_PATH, args.dir)
    else:
        run_dir = os.path.join(RESULT_PATH, starttime.strftime("%m-%d-%H-%M"))
    
    thread_count = args.threads if args.threads else THREAD_COUNT
    run_dir_exists = os.path.isdir(run_dir)
    init_log_handler(run_dir, run_dir_exists, thread_count)
    init_result_handler(run_dir, run_dir_exists, thread_count)

    if run_dir_exists:
        log_progress(f"Logging directory already exists, continuing execution.", 5)
    else:
        log_progress(f"Logging results in directory: {run_dir}", 4)

    # load chosen packages with programs and chosen query
    packages = load_packages(args.package, args.program)
    query = load_query(args.query)

    # copy query files to logging dir
    shutil.copy(query.file_path, run_dir)

    for i in range(thread_count):
        server_endpoint = f"localhost:808{i}"
        # connect to joern server
        clients.append(CPGQLSClient(server_endpoint))

    run_query_on_packages(packages, query, args.scope, thread_count)

    endtime = datetime.now()
    minutes, seconds = divmod((endtime - starttime).seconds, 60)
    log_summary(f"Total time: {minutes} min {seconds} s", 5)
    
    summarize_result(args.package, args.program, args.query, args.scope)


def run_query_on_packages(packages: Queue[Package], query: Query, scope: str, thread_count: int):    
    for i in range(thread_count):
        thread = threading.Thread(target=process_queue, args=(packages, query, scope, i), daemon=True)
        thread.start()
    packages.join() 
           
def process_queue(packages: Queue[Package], query: Query, scope: str, thread_id: int):
    while True:
        try:
            package = packages.get(block=True, timeout=0)
        except Empty:
            break
        try:
            run_query_on_package(package, query, scope, thread_id)
        except Exception as e:
            log_error(f"Exception in thread {thread_id}:\n {e}", 5, thread_id)
            log_error(f"{traceback.format_exc()}", 1, thread_id)
            packages.put(package)
            time.sleep(10)
        finally:
            packages.task_done()


def run_query_on_package(package: Package, query: Query, scope: str, thread_id: int) -> bool:
    log_progress(f"Thread {thread_id} handling package {package.name}", 5, thread_id)
    starttime = datetime.now()

    # logging file
    set_query_package_dir_for_thread(query.name, package.name, thread_id)

    programs_to_run = package.programs
    # load programs which were already completed on a previous run
    completed_programs = load_completed_programs_in_package_for_query(package.name, query.name)
    if completed_programs:
        log_progress(f"Found {len(completed_programs)} completed programs in package {package.name} for query {query.name}. Skipping them in this run", 5, thread_id)
        programs_to_run = [program for program in programs_to_run if not program.name in completed_programs]

    test_programs_positive, test_programs_negative, sample_programs = divide_programs_in_categories(programs_to_run, query)

    match scope:
        case "all":
            pass
        case "test":
            sample_programs = []
        case "positives":
            test_programs_negative = []
            sample_programs = []
        case "sample":
            test_programs_positive = []
            test_programs_negative = []
    program_count = len(test_programs_positive + test_programs_negative + sample_programs)
    if program_count == 0:
        log_progress(f"No programs in package {package.name} to run query {query.name} for the scope {scope}.", 5, thread_id)
        return False

    #import package
    if not open_cpg(package, thread_id):
        log_error(f"Not running query {query.name} for package {package.name}.", 7, thread_id)
        return False
    
    log_progress(f"Starting query {query.name} in package {package.name}", 7, thread_id)
    
    # common queries for whole package
    run_common_queries(query, thread_id)

    # program specific queries
    if len(test_programs_positive):
        log_progress(f"Starting query {query.name} for {len(test_programs_positive)} positives: {[program.name for program in test_programs_positive]}", 7, thread_id)
        for program in test_programs_positive:
            run_query_on_program(query, program, thread_id, True)
    if len(test_programs_negative):
        log_progress(f"Starting query {query.name} for {len(test_programs_negative)} negatives: {[program.name for program in test_programs_negative]}", 7, thread_id)           
        for program in test_programs_negative:
            run_query_on_program(query, program, thread_id, False)
    if len(sample_programs):
        log_progress(f"Starting query {query.name} for {len(sample_programs)} sample programs: {[program.name for program in sample_programs]}", 7, thread_id)           
        for program in sample_programs:
            run_query_on_program(query, program, thread_id, False)

    # delete package from workspace
    req = f"delete(\"{package.name}\")"
    execute_request(req, "DELETE", thread_id)
    endtime = datetime.now()
    minutes, seconds = divmod((endtime - starttime).seconds, 60)
    log_summary(f"Run for query {query.name} in package {package.name} complete: {minutes} min {seconds} s", 7, thread_id)
        
    return True

def run_common_queries(query: Query, thread_id: int):
    for query in query.common:
        execute_request(query, "COMMON", thread_id)

def run_query_on_program(query: Query, program: Program, thread_id: int, has_label: bool):
    log_progress(f"Starting query {query.name} in program {program.name}.", 7, thread_id)

    # object to store results for this program and query
    result = Result(program.name, program.package, query.name, program.test, has_label)

    # set the file with the main method for this program
    response = execute_program_request(f'val progPath = "{program.path}"', thread_id)
    
    for query in query.program:
        response = execute_program_request(query, thread_id)
        # timeout detected in response - save result so program is omitted in next execution
        if not eval_result(query, response, result):
            save_result(result, thread_id)
            # delete package from workspace
            req = f"delete(\"{program.package}.bin\")"
            execute_request(req, "DELETE", thread_id)
            raise TimeoutError(f"timeout in program {program.name}")

    # save result to yaml
    save_result(result, thread_id)

def open_cpg(package: Package, thread_id: int) -> bool:
    # delete package from workspace
    cpg_name = f"{package.path.split("/")[0]}.bin"

    #req = f"delete(\"{cpg_name}\")"
    #response = execute_request(req, "DELETE", thread_id)
    
    #path = os.path.join(SOURCE_CODE_PATH, package.path)
    #req = import_code_query(path, package.name, "C")

    path = os.path.join(SOURCE_CPG_PATH, cpg_name)
    req = f"importCpg(\"{path}\", \"{package.name}\")"
    
    #req = f"importCpg(\"{path}\", \"{package.name}{datetime.now().strftime("%m%d%H%M")}\")"
    response = execute_request(req, "IMPORT", thread_id)
    # check if CPG was generated
    if cpg_was_generated(response):
        log_success(f"Importing CPG for package {package.name} succeeded.", 7, thread_id)
        return True
    else:
        log_error(f"Importing CPG for package {package.name} with path {path} failed.", 7, thread_id)
        return False

def execute_request(req: str, query_tag: str, thread_id: int) -> str:
    log_request(req, query_tag, 3, thread_id)
    response = clean_response(clients[thread_id].execute(req))
    log_response(response, 3, thread_id)
    return response

def execute_program_request(req: str, thread_id: int) -> str:
    log_request(req, "PROGRAM", 3, thread_id)
    try:
        response = clean_response(clients[thread_id].execute(req, timeout=TIMEOUT))
        log_response(response, 3, thread_id)
    except TimeoutError:
        log_error(f"Timeout after {TIMEOUT} seconds while executing request {req}", 7, thread_id)
        variable  = get_variable_name_from_scala_request(req)
        response = f"{variable}: timeout"
        log_response(response, 3, thread_id)
        #kill server so it can restart and process further requests
        if not remote:
            kill_joern_server(thread_id)
        
        # if timeout occured during query, set the value to empty list so other queries using this variable can still run
        #dummy_variable_req = f"val {variable} = List()"
        #log_request(dummy_variable_req, "PROGRAM", 3, thread_id)
        #dummy_response = clean_response(clients[thread_id].execute(dummy_variable_req))
        #log_response(dummy_response, 3, thread_id)
    return response

# load packages with their respective programs
# filter the list for packages/ programs passed as parameters and put them in a queue
def load_packages(chosen_package_names: List[str], chosen_program_names: List[str]) -> Queue[Package]:
    packages = load_packages_with_programs(chosen_program_names)
    package_queue = Queue()
    for package in packages:
        if not chosen_package_names or package.name in chosen_package_names:
            package_queue.put(package)
    return package_queue

# load queries and filter for those passed as parameter
def load_query(query_name: str):
    query = get_query(query_name)
    if not query:
        log_error("Query with given name does not exist")
        exit()
    return query

# return list of programs that match given scope, remove duplicates (=programs with same path as a different program) from the list
def divide_programs_in_categories(programs: list[Program], query: Query) -> tuple[list[Program], list[Program], list[Program]]:
    programs_without_duplicates = [program for program in programs if not program.path.startswith("duplicate")]
    match query.name:
        case "shell":
            test_programs_positive = [program for program in programs_without_duplicates if program.type == "test" and ("shell" in program.labels or "command" in program.labels)]
            test_programs_negative = [program for program in programs_without_duplicates if program.type == "test" and ("shell" not in program.labels and "command" not in program.labels)]
        case "reverse_shell":
            test_programs_positive = [program for program in programs_without_duplicates if program.type == "test" and ("reverse_shell" in program.labels or "non-interactive_reverse_shell" in program.labels)]
            test_programs_negative = [program for program in programs_without_duplicates if program.type == "test" and ("reverse_shell" not in program.labels and "non-interactive_reverse_shell" not in program.labels)]
        case "bind_shell":
            test_programs_positive = [program for program in programs_without_duplicates if program.type == "test" and ("bind_shell" in program.labels or "non-interactive_bind_shell" in program.labels)]
            test_programs_negative = [program for program in programs_without_duplicates if program.type == "test" and ("bind_shell" not in program.labels and "non-interactive_bind_shell" not in program.labels)]
        case _:  
            test_programs_positive = [program for program in programs_without_duplicates if program.type == "test" and query.name in program.labels]
            test_programs_negative = [program for program in programs_without_duplicates if program.type == "test" and query.name not in program.labels]
    sample_programs = [program for program in programs_without_duplicates if program.type == "sample"]
    return (test_programs_positive, test_programs_negative, sample_programs)

def kill_joern_server(thread_id : int):
    port = f"808{thread_id}"
    log_progress(f"Killing joern server on port {port}", 7, thread_id)

    command = f"for pid in $(ps -ef | grep \"joern\" | grep \"[s]erver-port {port}\" | awk '{{print $2}}'); do kill -9 $pid; done"
    os.system(command)


def parse_args():
    # all arguments are optional
    parser = argparse.ArgumentParser()

    package_or_program = parser.add_mutually_exclusive_group()
    package_or_program.add_argument(
        "--package",        
        nargs = "*",
        type=str,
        default=None,
        help="Run query only on the given package or packages")
    package_or_program.add_argument(
        "--program",
        nargs = "*",
        type=str,
        default=None,
        help="Run query only on the given program or programs")
    parser.add_argument(
        "--scope",
        choices=['all', 'test', 'positives', 'sample'],
        default='all',
        help="Run query only on " \
        "<test> test programs (programs listed on gtfobins.com) " \
        "<positives> test programs with label of the current query (positives) " \
        "<sample> new programs (not listed on gtfobins.com")
    
    parser.add_argument(
        "--query",
        required=True,
        type=str,
        default=None,
        help="Run the given query")
    parser.add_argument(
        "--dir",
        type=str,
        default=None,
        help="Run/continue the queries for the given directory")
    parser.add_argument(
        "--threads",
        type=int,
        default=None,
        help="Thread count")
    parser.add_argument(
        "--remote",
        type=bool,
        default=False,
        help="Remote execution - no killing of joern servers when timeout occurs")
    try:
        args = parser.parse_args()
    except:
        parser.print_help()
        exit()
    else:
        return args
    
if __name__ == "__main__":
    main()
