import os
from datetime import datetime
import re

# logging levels (use bitwise and)
GENERAL_LOG = 1
QUERY_PACKAGE_LOG = 2
CONSOLE_LOG = 4
RESULT_LOG = 8

log_dir: str = None

#general logs
general_log_path: str = None
result_summary_log_path: str = None

#logs for the threads
thread_log_paths = []
query_package_log_paths = []

def init_log_handler(dir: str, dir_exists: bool, thread_count: int):
    global log_dir
    log_dir = os.path.join(dir, "logs")
    global thread_log_paths
    thread_log_paths = [os.path.join(log_dir, f"thread_{i}.log") for i in range(thread_count)]
    global general_log_path
    general_log_path = os.path.join(log_dir, "general.log")
    if not dir_exists:
        os.makedirs(log_dir)
    global query_package_log_paths 
    query_package_log_paths = [None] * thread_count

def set_query_package_dir_for_thread(query_name: str, package_name: str, thread_id: int):
    log_path = f"{query_name}_{package_name}.log"
    global query_package_log_paths
    query_package_log_paths[thread_id] = os.path.join(log_dir, log_path)

#called by result handler
def set_result_summary_path(path: str):
    global result_summary_log_path
    result_summary_log_path = path

def log_progress(text: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "PROGRESS", log_level)

def log_summary(text: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "SUMMARY", log_level)

def log_error(text: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "ERROR", log_level)

def log_success(text: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "SUCCESS", log_level)

def log_request(text: str, query_tag: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "REQUEST", log_level, query_tag)

def log_response(text: str, log_level: int = 4, thread_id: int = None):
    log(text, thread_id, "RESPONSE", log_level)

def log(text: str, thread_id: int, type: str, log_level: int, query_tag: str = None):
    current_time = datetime.now().strftime("%d.%m %H:%M:%S")
    
    if log_level & 1 and thread_id != None:
        write_to_file(thread_log_paths[thread_id], text, type, current_time, query_tag)
    if log_level & 1 and thread_id == None:
        write_to_file(general_log_path, text, type, current_time, query_tag)
    if log_level & 2:
        write_to_file(query_package_log_paths[thread_id], text, type, current_time, query_tag)
    if log_level & 8:
        write_to_file(result_summary_log_path, text, type)

    #console logging - all threads
    log_text = f"[{current_time}]{f"[Thread {thread_id}]" if thread_id != None else ""} {text}"
    if log_level & 4:
        match type:
            case "PROGRESS":
                print_cyan(log_text)
            case "SUMMARY":
                print_purple(log_text)
            case "ERROR":
                print_red(log_text)
            case "SUCCESS":
                print_green(log_text)
            case "REQUEST":
                print_yellow(log_text)
            case "RESPONSE":
                print_yellow(log_text)

def write_to_file(file_path: str, text: str, type: str, current_time: str = None, query_tag: str = None):
    log_start = f"{f"[{current_time}]" if current_time else ""}[{type}]{f"[{query_tag}]" if query_tag else ""}"
    log_end = f"{f"[{query_tag}]" if query_tag else ""}[{type}]"
    log_complete = f"{log_start} {text} {log_end}\n"
    with open(file_path, "a") as file:
        file.write(log_complete)

def clean_response(result: dict) -> str:
    text = ""
    try:
        text = result["stdout"]
    except KeyError:
        log_error(f"Result has no key \"stdout\": {result}")
        text = result
    ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')
    text = ansi_escape.sub('', text)
    return text

def print_green(s): print("\033[92m{}\033[00m".format(s))
def print_red(s): print("\033[91m{}\033[00m".format(s))
def print_cyan(s): print("\033[96m{}\033[00m".format(s))
def print_purple(s): print("\033[95m{}\033[00m".format(s))
def print_yellow(s): print("\033[93m{}\033[00m".format(s))