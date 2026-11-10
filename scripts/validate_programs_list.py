from typing import List
from utils.models import Program
from utils.log_handler import *
from utils.data_handler import get_complete_path, load_programs
import os

# list to store all programs
programs: List[Program]

def main():
    global programs
    programs = load_programs()

    print_cyan(f"Found {len(programs)} binaries in total (with and without label).")

    with_labels = sum(1 for program in programs if program.labels)
    print_cyan(f"{with_labels} binaries have labels assigned (from https://gtfobins.github.io/)")
    
    with_labels_and_path = sum(1 for program in programs if program.labels and program.path)
    with_labels_and_path_per = 100*(with_labels_and_path/with_labels)
    print_green(f"{with_labels_and_path} ({with_labels_and_path_per:.1f}%) of the gtfobins binaries have paths assigned.")

    test_programs = sum(1 for program in programs if program.type == "test")
    print_green(f"{test_programs} of the gtfobins binaries are marked as test programs.")

    not_testable_programs = [program.name for program in programs if program.type.startswith("not testable")]
    not_testable_programs_per = 100*(len(not_testable_programs)/with_labels)
    print_yellow(f"{len(not_testable_programs)} ({not_testable_programs_per:.1f}%) of the gtfobins binaries can not be analyzed (e.g. not written in C).")
    print_yellow(not_testable_programs)

    to_do_test_programs = [program.name for program in programs if program.type.upper() == "TODO" and program.labels]
    to_do_test_programs_per = 100*(len(to_do_test_programs)/with_labels)
    print_red(f"{len(to_do_test_programs)} ({to_do_test_programs_per:.1f}%) of the gtfobins binaries are TODO:")
    print_red(to_do_test_programs)

    sample_programs = [program.name for program in programs if program.type == "sample" and program.path]
    print_purple(f"There are {len(sample_programs)} sample programs with assigned paths")

    to_do_sample_programs = [program.name for program in programs if program.type.upper() == "TODO" and not program.labels]
    to_do_sample_programs_per = 100*(len(to_do_sample_programs)/with_labels)
    print_red(f"{len(to_do_sample_programs)} ({to_do_sample_programs_per:.1f}%) of the sample binaries are TODO:")
    print_red(to_do_sample_programs)

    print_package_stats()
    print_label_stats()
    validate_paths()
    validate_type()

def print_package_stats():
    print(f"{40*"*"} PACKAGE STATS {40*"*"}")
    packages = { program.package for program in programs if program.package }
    for package in packages:
        programs_in_package = [program.name for program in programs if program.package == package]
        with_labels = sum(1 for program in programs if program.package == package and program.labels)
        print_cyan(f"Found {len(programs_in_package)} programs in package: {package} ({with_labels} have labels)")
        #for program in programs_in_package:
        #    print(program)
        #print_cyan(f"{programs_in_package}")

def print_label_stats():
    print(f"{40*"*"} LABEL STATS {40*"*"}")
    labels = { label for program in programs for label in program.labels }
    for label in labels:
        programs_with_label = [program.name for program in programs if label in program.labels]
        test_programs_with_label = [(program.name, program.package, program.path) for program in programs if label in program.labels and program.type == "test"]
        print_green(f"Found {len(programs_with_label)} programs with label: {label}")
        print_cyan(f"{len(test_programs_with_label)} are testable: {test_programs_with_label}")


def validate_paths():
    print(f"{40*"*"} VALIDATING PATHS {40*"*"}")
    for program in programs:
            if program.type.startswith("not testable"):
                continue
            if program.package and not program.path:
                print_red(f"Program {program.name} in package {program.package} has no path")
            elif program.path:
                for other_program in programs:
                    if other_program.package != program.package or other_program == program:
                        continue
                    elif program.path == other_program.path and not program.path.startswith("duplicate"):
                        print_red(f"Program {program.name} in package {program.package} has the same path: \"{program.path}\" as program {other_program.name}.")
                path = get_complete_path(program)
                if not path:
                    print_red(f"Program {program.name} in package {program.package} has invalid path: {path}")
                elif program.path.startswith("duplicate"):
                    continue
                elif not os.path.isfile(path):
                    print_red(f"Program {program.name} in package {program.package} has invalid path: {path}")

def validate_type():
    print(f"{40*"*"} VALIDATING TYPES {40*"*"}")
    for program in programs:
        if program.type == "test" and not program.labels:
            print_red(f"Program {program.name} has type test but no labels")
        elif program.type == "sample" and program.labels:
            print_red(f"Program {program.name} has type sample but also labels")
        elif program.type not in ["test", "sample"] and not program.type.startswith("not testable"):
            print_red(f"Program {program.name} has wrong type {program.type}")

if __name__ == "__main__":
    main()