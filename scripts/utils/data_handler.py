from dataclasses import asdict
import os
from utils.models import *
from utils.paths import *
import yaml

packages: List[Package] = None
programs: List[Program] = None
known_limitations: List[Limitation] = None

def get_complete_path(program: Program) -> str:
    load_packages_with_programs()
    global packages
    for package in packages:
        if program.package == package.name:
            return os.path.join(BASE_PATH, SOURCE_CODE_PATH, package.path, program.path)
        
def get_programs_with_label(label: str) -> List[Program]:
    load_programs()
    programs_with_label = []
    for program in programs:
        if label in program.labels:
            programs_with_label.append(program)
    return programs_with_label
        
def load_packages_with_programs(programs: List[str] = []) -> List[Package]:
    global packages
    if not packages:
        with open(PACKAGE_LIST_PATH, "r") as file:
            data = yaml.safe_load(file)
            packages = [Package(**item) for item in data]
    for program in load_programs():
        if programs and program.name not in programs:
            continue
        if program.package and program.path:
            package = next((package for package in packages if package.name == program.package), None)
            if package:
                package.programs.append(program)
    packages = [package for package in packages if len(package.programs)]
    return packages

def load_programs() -> List[Program]:
    global programs
    if not programs:
        with open(PROGRAM_LIST_PATH, "r") as file:
            data = yaml.safe_load(file)
            programs = [Program(**item) for item in data]
    return programs

def get_query(query_name: str) -> Query:
    try:
        return Query(query_name, f"{os.path.join(QUERIES_PATH, query_name)}.sc")
    except:
        return None

def load_known_limitations() -> List[Limitation]:
    global known_limitations
    if not known_limitations:
        with open(LIMITATION_LIST_PATH, "r") as file:
            data = yaml.safe_load(file)
            known_limitations = [Limitation(**item) for item in data]
    return known_limitations

def check_for_known_limitations(program: str, query: str) -> bool:
    load_known_limitations()
    for limitation in known_limitations:
        if program == limitation.program and query in limitation.labels:
            return True
    return False
