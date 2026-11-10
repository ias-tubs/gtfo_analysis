import re
from typing import List


def cpg_was_generated(scala: str) -> bool:
    return re.search(r"Cpg\[Graph\[\d* nodes\]\]", scala)

# def convert_to_scala_list_string(python: List) -> str:
#     conv = re.sub(r"(\[|\])", "", str(python))
#     return f"List({re.sub(r"'", "\"", conv)})"

def get_int_from_scala_int(scala: str) -> int:
    match = re.search(r"Int = (\d*)", scala)
    number = match.group(1) if match else "0"
    return int(number)

def get_variable_name_from_scala_length_request(scala: str) -> str:
    return scala.split(".")[0]

def get_variable_name_from_scala_request(scala: str) -> str:
    return scala.split("=")[0].removeprefix("val").strip()