import argparse
from dataclasses import asdict
from enum import Enum
import os
from typing import List

import yaml
from utils.data_handler import check_for_known_limitations, load_packages_with_programs, load_programs
from utils.log_handler import *
from utils.scala_parser import *
from utils.models import Classification, Package, Program, Query, Result
from utils.paths import RESULT_PATH


result_dir: str = None
result_yaml_path: str = None
result_yaml_paths = []
result_summary_path: str = None
query_result_path: str = None

def init_result_handler(dir: str, dir_exists: bool, thread_count: int):
    global result_dir
    global result_yaml_path
    global result_yaml_paths
    global result_summary_path
    result_dir = os.path.join(dir, "results")
    result_yaml_path = os.path.join(result_dir, "results.yaml")
    result_yaml_paths = [os.path.join(result_dir, f"thread_{i}_results.yaml") for i in range(thread_count)]

    result_summary_path = os.path.join(result_dir, "summary.log")
    if not dir_exists:
        os.makedirs(result_dir)
    set_result_summary_path(result_summary_path)

def set_query_package_dir_for_thread(query_name: str, package_name: str):
    global result_dir
    global query_result_path
    path = f"{query_name}_{package_name}.log"
    query_result_path = os.path.join(result_dir, path)

def eval_result(req: str, response: str, result: Result) -> bool:
    if response.endswith("timeout"):
        variable = get_variable_name_from_scala_request(req)
        result.meta_results[variable] = "timeout"
        result.assign_final_results("timeout")
        return False
    elif not req.endswith("length"):
        return True
    else:
        length = get_int_from_scala_int(response)
        variable = get_variable_name_from_scala_length_request(req)
        result_exists = (length != 0)
        if result_exists  or "resultFlows.length" in req:
            result.meta_results[variable] = result_exists
        if "resultFlows.length" in req:
            result.assign_final_results(result_exists)
        return True

def save_result(result: Result, thread_id: int):
    result_dict = [asdict(result, dict_factory=custom_dict_factory)]
    with open(result_yaml_paths[thread_id], "a") as file:
        yaml.dump(result_dict, file, sort_keys=False)

def load_completed_programs_in_package_for_query(package_name: str, query_name: str) -> List[(str)]:
    results = load_results()
    return [result.program for result in results if result.package == package_name and result.query == query_name]

def load_results() -> List[Result]:
    results = []
    #load results from all thread specific result files
    for file in os.listdir(result_dir):
        if not file.endswith("yaml"):
            continue
        with open(os.path.join(result_dir, file), "r") as file:
            data = yaml.safe_load(file)
            if data:
                results += [Result.from_dict(item) for item in data]
    #same results might be in thread specific and global result file
    results = remove_duplicate_results(results)
    return results

def summarize_result(packages: List[str], programs: List[str], query: str, scope: str):
    log_summary(f"Run query {query}")
    log_summary(f"on packages: {f"{packages}" if packages else "all"}")
    log_summary(f"on programs: {f"{programs}" if programs else "all"}")
    log_summary(f"and scope: {scope}")
    results = load_results()
    duplicate_programs = get_duplicate_programs(results)
    #check if all results for given parameters are present
    check_if_results_complete(packages, programs, query, scope, results)
    #save all results into global result file
    result_dict = [asdict(result, dict_factory=custom_dict_factory) for result in results]
    with open(result_yaml_path, "w") as file:
        yaml.dump(result_dict, file, sort_keys=False)

    check_for_duplicate_results(results)
    queries_run = {result.query for result in results}
    if len(queries_run) != 1:
        log_error(f"Results for multiple queries exist in the same file", 12)
    log_summary(f"Following queries were run: {queries_run}", 12)
    for query_name in queries_run:
        summarize_query(results, query_name, duplicate_programs)

def summarize_query(results: List[Result], query_name: str, duplicate_programs: dict[str, list[str]]):
    results = [result for result in results if result.query == query_name]
    log_summary(f"{50*"*"} {query_name.upper()} {50*"*"}", 12)
    log_summary(f"Run query {query_name} on {len(results)} program(s) in total.", 12)

    timeout_test_p = [result.program for result in results if result.final_result == "timeout" and result.test_program == True and result.has_label == True]
    timeout_test_n = [result.program for result in results if result.final_result == "timeout" and result.test_program == True and result.has_label == False]
    timeout_sample = [result.program for result in results if result.final_result == "timeout" and result.test_program == False]

    log_error(f"Timeout occured on {len(timeout_test_p + timeout_test_n + timeout_sample)} programs in total:", 12)
    log_error(f"{len(timeout_test_p)} positive test programs with timeout: {timeout_test_p}", 12)
    log_error(f"{len(timeout_test_n)} negative test programs with timeout: {timeout_test_n}", 12)
    log_error(f"{len(timeout_sample)} sample programs with timeout: {timeout_sample}", 12)


    test_results = [result for result in results if result.test_program]
    sample_results = [result for result in results if not result.test_program]
    if test_results:
        summarize_test_results(test_results)
    else:
        log_summary("No results for test programs", 12)

    if sample_results:
        summarize_sample_results(sample_results)
    else:
        log_summary("No results for sample programs", 12)
    
    if duplicate_programs:
        summarize_duplicate_programs(results, duplicate_programs)

def summarize_test_results(test_results: List[Result]):
    log_summary(f"{50*"*"} TEST PROGRAMS {50*"*"}", 12)
    count_positives = sum(1 for result in test_results if result.has_label)
    count_negatives = sum(1 for result in test_results if not result.has_label)
    log_summary(f"Run query on {count_positives} positive and {count_negatives} negative test programs in total.", 12)

    counts = dict.fromkeys(Classification)
    for cla in Classification:
        results_with_classification = [result for result in test_results if result.classification == cla]
        counts[cla] = len(results_with_classification)
        log = f"Found {len(results_with_classification)} {cla.value}(s)."

        problems = sum(1 for result in results_with_classification if check_for_known_limitations(result.program, result.query))
        if problems:
            log += f" ({problems} programs have known limitations)"
        log_success(log, 12) if "True" in cla.value else log_error(log, 12)
    precision = 100 * counts[Classification.TP] / (counts[Classification.TP] + counts[Classification.FP]) if counts[Classification.TP] else 0
    recall = 100 * counts[Classification.TP] / (counts[Classification.TP] + counts[Classification.FN]) if counts[Classification.TP] else 0
    log_summary(f"Precision: {precision:.1f}%", 12)
    log_summary(f"Recall: {recall:.1f}%", 12)

    packages_run = sorted({result.package for result in test_results})
    true_positives = []
    false_negatives = []
    false_positives = []
    for package in packages_run:
        for cla in Classification:
            results_with_classification = [result for result in test_results if result.classification == cla and result.package == package]
            without_limitations = [result.program for result in results_with_classification if not check_for_known_limitations(result.program, result.query)]
            with_limitations = [result.program for result in results_with_classification if check_for_known_limitations(result.program, result.query)]
            c_without_limitations = len(without_limitations)
            c_with_limitations = len(with_limitations)
            count = c_without_limitations + c_with_limitations
            if cla == Classification.TP:
                true_positives += [(result.program, package) for result in results_with_classification]
            if cla == Classification.FP:
                false_positives += [(result.program, package) for result in results_with_classification]
            if cla == Classification.FN:
                false_negatives += [(result.program, package) for result in results_with_classification]  
            if count:
                log = f"Found {count} {cla.value}(s) in package {package}:"
                if not c_with_limitations:
                    log += f" {without_limitations}"
                else:
                    log += f"\n{c_without_limitations} without known limitations: {without_limitations}"
                    log += f"\n{c_with_limitations} with known limitations: {with_limitations}"
                log_success(log, 12) if "True" in cla.value else log_error(log, 12)
        results_with_timeout = [result for result in test_results if result.package == package and result.final_result == "timeout"]
        if len(results_with_timeout):
            log_error(f"Package {package}: Timeout occured at least once when running queries for {len(results_with_timeout)} program(s): {[result.program for result in results_with_timeout]}")

    log_success(f"True positives: ({len(true_positives)})",12)
    for program, package in true_positives:
        log_success(f"- {program} ({package})",12)
    log_error(f"False negatives: ({len(false_negatives)})",12)
    for program, package in false_negatives:
        log_error(f"- {program} ({package})",12)
    log_error(f"False positives: ({len(false_positives)})",12)
    for program, package in false_positives:
        log_error(f"- {program} ({package})",12)



def summarize_sample_results(sample_results: List[Result]):
    log_summary(f"{50*"*"} SAMPLE PROGRAMS {50*"*"}", 12)

    log_summary(f"Run query on {len(sample_results)} sample programs in total.", 12)
    sample_positives = [result.program for result in sample_results if result.final_result == True]
    log_summary(f"Found {len(sample_positives)} positives among the sample programs in total.", 12)

    packages_run = sorted({result.package for result in sample_results})
    for package in packages_run:
        positive_sample_results_in_package = [result for result in sample_results if result.final_result == True and result.package == package]
        if positive_sample_results_in_package:
            for result in positive_sample_results_in_package:
                log_summary(f"- {result.program} ({package})",12)
        
        results_with_timeout = [result for result in sample_results if result.package == package and result.final_result == "timeout"]
        if len(results_with_timeout):
            log_error(f"Package {package}: Timeout occured at least once when running queries for {len(results_with_timeout)} program(s):")
            log_error([result.program for result in results_with_timeout])
            #log_summary(f"Found {len(positive_sample_results_in_package)} positive results among the sample programs in package {package}:", 12)
            #log_summary(f"{[result.program for result in positive_sample_results_in_package]}", 12)

def summarize_duplicate_programs(results: List[Result], duplicate_programs: dict[str, list[Program]]):
    log_summary(f"{50*"*"} DUPLICATE PROGRAMS {50*"*"}", 12)
    log_summary(f"Results for {sum([1 for program_list in duplicate_programs.values() for program in program_list])} duplicate programs in total.", 12)
    for main_program in duplicate_programs.keys():
        for dup_program in duplicate_programs[main_program]:
            for result in results:
                if main_program == result.program:
                    dup_result = Result(dup_program.name, dup_program.package, result.query, dup_program.test, (result.query in dup_program.labels), result.meta_results)
                    dup_result.assign_final_results(result.final_result)
                    log_summary(f"{dup_program.type} program {dup_result.program} - duplicate of program {main_program} has result {dup_result.classification}", 12)


def check_for_duplicate_results(results: List[Result]):
    uniques = set()
    duplicates = []
    for result in results:
        if result in uniques:
            duplicates.append((result.program, result.query))
        else:
            uniques.add(result)
    if duplicates:
        log_error(f"Found duplicates in results: {duplicates}", 12)

def remove_duplicate_results(results: List[Result]):
    results_without_duplicates = []
    for result in results:
        if not result in results_without_duplicates:
            results_without_duplicates.append(result)
    return results_without_duplicates

def get_duplicate_programs(results: List[Result]) -> dict[str, List[Program]]:
    duplicate_dict = {}
    programs_with_results = [result.program for result in results]
    all_programs = load_programs()
    for program in programs_with_results:
        duplicates = []
        for dup_program in all_programs:
            if dup_program.path and dup_program.path.startswith("duplicate") and program == dup_program.path.split(":")[1]:
                duplicates.append(dup_program)
        if duplicates:
            duplicate_dict[program] = duplicates
    return duplicate_dict

def check_if_results_complete(chosen_packages: List[str], chosen_programs: List[str], query: str, scope: str, results: List[Result]):
    all_programs = load_programs()
    #remove not testable programs
    all_programs = [program for program in all_programs if not program.type.startswith("not testable")]
    #filter for chosen programs
    if chosen_programs:
        all_programs = [program for program in all_programs if program.name in chosen_programs]
    #filter for chosen packages
    if chosen_packages:
        all_programs = [program for program in all_programs if program.package in chosen_packages]
    #filter for chosen scope
    if scope == "test":
        all_programs = [program for program in all_programs if program.type == "test"]
    elif scope == "positives":
        all_programs = [program for program in all_programs if program.type == "test" and query in program.labels]
    elif scope == "sample":
        all_programs = [program for program in all_programs if program.type == "sample"]
    #remove duplicates
    all_programs = [program for program in all_programs if not program.path.startswith("duplicate")]

    count = 0

    for program in all_programs:
        result_present = False
        for result in results:
            if result.program == program.name and result.query == query:
                result_present = True
                break
        if not result_present:
            count += 1
            log_error(f"Result for program {program.name} in package {program.package} for query {query} missing.", 12)
    
    log_summary(f"Results for {count} programs missing.", 12)

def custom_dict_factory(data):
    return dict((k, v if not isinstance(v, Enum) else v.value) for k, v in data)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--dir",
        type=str,
        required=True,
        help="Summarize the results for the given directory")
    package_or_program = parser.add_mutually_exclusive_group()
    package_or_program.add_argument(
        "--package",        
        nargs = "*",
        type=str,
        default=None,
        help="Check the results for the given package or packages")
    package_or_program.add_argument(
        "--program",
        nargs = "*",
        type=str,
        default=None,
        help="Check the results for the given program or programs")
    parser.add_argument(
        "--scope",
        choices=['all', 'test', 'positives', 'sample'],
        default='all',
        help="Check the results for the given scope")
    
    parser.add_argument(
        "--query",
        required=True,
        type=str,
        default=None,
        help="Check the results for the given query")
    try:
        args = parser.parse_args()
    except:
        parser.print_help()
    init_result_handler(os.path.join(RESULT_PATH, args.dir), True, 0)
    summarize_result(args.package, args.program, args.query, args.scope)
