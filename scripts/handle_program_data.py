import requests
from bs4 import BeautifulSoup
import yaml
from dataclasses import asdict
from typing import List
from utils.models import *
from utils.paths import *
import argparse
import re

# gtfobins url
URL = "https://gtfobins.github.io"

# list to store all programs
programs: List[Program]

def main():
    # get the programs from the yaml file
    global programs
    programs = get_existing_program_list()

    # get programs from gtfobins.github.io and add info to existing list
    add_gtfobins_info()

    # optionally get programs from file (command arguments)
    add_file_info()
    
    # sort by name
    programs.sort(key=lambda p: p.name)
    # write result to yaml file
    programs_dict = [asdict(p) for p in programs]
    with open(PROGRAM_LIST_PATH, "w") as file:
        yaml.dump(programs_dict, file, sort_keys=False)

    # log info in terminal
    print(f"Found {len(programs)} binaries in total.")
    
def get_existing_program_list():
    with open(PROGRAM_LIST_PATH, "r") as file:
        data = yaml.safe_load(file)
        return [Program(**item) for item in data]

def add_gtfobins_info():
    page = requests.get(URL)
    soup = BeautifulSoup(page.content, "html.parser")

    # get all "tr" elements for bins
    gtfo_soup = [tr for tr in soup.find_all("tr") if tr.findChild("a", class_ = "bin-name")]

    # extract program info (name+labels) from soup and add to programs list
    for gtfo_bin in gtfo_soup:
        name = gtfo_bin.findChild("a", class_ = "bin-name").text
        labels = [label.text.lower().replace(' ', '_') for label in gtfo_bin.findChildren("li", recursive=True)]
        new_program = Program(name=name, labels=labels)
        add_program_to_existing_programs(new_program)

def add_file_info():
    # check if file was passed as program argument
    parser = argparse.ArgumentParser()
    parser.add_argument("--file")
    parser.add_argument("--package")
    args = parser.parse_args()
    if (args.file != None and args.package != None):
        get_package_programs(args) 

def get_package_programs(args):
    # convert file with program names (separated by ",", space or newline) to list with programs
    # extracted info: program name and package
    with open(args.file, "r") as file:
        content = file.read()
    programs = re.split(r"[,\s]+", content.strip())
    package_programs = [Program(name=name, package=args.package) for name in programs]
    for new_program in package_programs:
        add_program_to_existing_programs(new_program)

def add_program_to_existing_programs(new_program: Program):
    # add program to program list - if program is already present, info is merged
    existing_program = next((existing_program for existing_program in programs if existing_program == new_program), None)
    if existing_program:
        existing_program.merge(new_program)
    else:
        print(f"Adding new binary: {new_program}")
        programs.append(new_program)

if __name__ == "__main__":
    main()