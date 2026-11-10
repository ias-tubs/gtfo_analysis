from dataclasses import dataclass, field
from enum import Enum
from typing import List

@dataclass
class Program:
    name: str
    package: str = field(default=None)
    path: str = field(default=None)
    labels: List[str] = field(default_factory=list)
    type: str = field(default="TODO")
    def __eq__(self, other):
        return self.name == other.name
    def merge(self, other):
        if other.package and self.package and other.package != self.package:
            print(f"Problem merging {self.name}: 2 packages assigned: {other.package} and {self.package}")
        self.package = other.package if other.package else self.package
        if other.path and self.path and other.path != self.path:
            print(f"Problem merging {self.name}: 2 paths assigned: {other.path} and {self.path}")
        self.path = other.path if other.path else self.path
        if other.labels and self.labels and other.labels != self.labels:
            print(f"Problem merging {self.name}: 2 labels lists assigned: {other.labels} and {self.labels}")
        self.labels = other.labels if len(other.labels) > len(self.labels) else self.labels
    def __post_init__(self):
        self.test = len(self.labels) > 0

@dataclass
class Package:
    name: str
    path: str
    programs: List[Program] = field(default_factory=list)

@dataclass
class Query:
    name: str
    file_path: str
    source: List[str] = field(init=False, default_factory=list)
    common: List[str] = field(init=False, default_factory=list)
    program: List[str] = field(init=False, default_factory=list)
    # read query from file and add every line as list item
    def __post_init__(self):
        with open(self.file_path) as f:
            for line in f:
                if line.strip().startswith("/* COMMON */"):
                    self.common.append(line.removeprefix("/* COMMON */").strip())
                elif line.strip().startswith("/* PROGRAM */"):
                    self.program.append(line.removeprefix("/* PROGRAM */").strip())

class Classification(Enum):
    TP = "True positive"
    TN = "True negative"
    FP = "False positive"
    FN = "False negative"

@dataclass
class Result:
    program: str
    package: str
    query: str
    test_program : bool = field(compare=False)
    has_label: bool = field(compare=False)
    meta_results: dict[str, bool | str ] = field(default_factory=dict, compare=False)
    final_result: bool | str = field(default=None, compare=False)
    classification: Classification = field(default=None, repr=False, compare=False)
    
    def assign_final_results(self, result):
        self.final_result = result
        if not self.test_program or result == "timeout":
            return
        elif result:
            self.classification = Classification.TP if self.has_label else Classification.FP
        else:
            self.classification = Classification.TN if not self.has_label else Classification.FN

    def __hash__(self):
        return hash((self.program, self.package, self.query))
    
    @classmethod
    def from_dict(cls, d: dict):
        if "classification" in d and isinstance(d["classification"], str):
            d["classification"] = Classification(d["classification"])
        return cls(**d)

@dataclass
class Limitation:
    program: str
    labels: List[str]
    type: str = field(default=None)
    details: str = field(default=None)