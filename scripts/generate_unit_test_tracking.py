#!/usr/bin/env python3
"""Regenerate JaMuz/UNIT_TEST_TRACKING.csv from tests and main classes."""
import csv
import os
import re

ROOT_TEST = os.path.join(os.path.dirname(__file__), "..", "src", "test", "java")
ROOT_MAIN = os.path.join(os.path.dirname(__file__), "..", "src", "main", "java")
OUT = os.path.join(os.path.dirname(__file__), "..", "UNIT_TEST_TRACKING.csv")


def infer_junit_from_test(path: str) -> str:
    if not os.path.isfile(path):
        return "none"
    text = open(path, encoding="utf-8", errors="replace").read()
    jup = "org.junit.jupiter" in text
    j4 = bool(re.search(r"import org\.junit\.(\*|Test)", text))
    return "junit5" if jup else ("junit4" if j4 else "unknown")


def analyze_test_file(path: str) -> tuple[str, ...]:
    rel = os.path.relpath(path, ROOT_TEST).replace(os.sep, "/")
    text = open(path, encoding="utf-8", errors="replace").read()
    proto = len(re.findall(r'fail\("The test case is a prototype', text))
    fixme = len(re.findall(r"FIXME TEST", text))
    ign = len(re.findall(r"@Ignore", text))
    println = len(re.findall(r"System\.out\.println", text))
    tests = len(re.findall(r"^\s*@Test\b", text, re.M)) + len(
        re.findall(r"^\s*@org\.junit\.jupiter\.api\.Test\b", text, re.M)
    )
    jup = "org.junit.jupiter" in text
    j4 = bool(re.search(r"import org\.junit\.(\*|Test)", text))
    junit = "junit5" if jup else ("junit4" if j4 else "unknown")
    scope = "functional" if rel.startswith("tests/functional/") else "unit"

    if scope == "functional":
        status, harmonize, gaps = "out_of_unit_scope", "Track separately (functional plan / TestPlan)", "—"
    elif proto >= tests and tests > 0:
        status = "blocked_prototypes"
        harmonize = "Replace NetBeans stubs with real tests or remove; explicit names (should…When…)"
        gaps = "Define expected behavior (DAO/API)"
    elif proto > 0:
        status = "mixed_prototypes"
        harmonize = "Remove remaining stubs; reduce println; explicit names"
        gaps = "Negative cases + constraints (see FIXME in file)"
    elif fixme > 0:
        status = "to_complete_fixme"
        harmonize = (
            "Address FIXME; pick JUnit alignment (this file alone is JUnit 5)"
            if junit == "junit5"
            else "Address FIXME; should…When… names; arrange-act-assert for large tests"
        )
        gaps = "Read FIXME TEST comments in the file"
    elif println > 8:
        status = "review_logs"
        harmonize = "Replace System.out with assertions or test logging"
        gaps = "Check stability / determinism"
    else:
        status = "rather_clean"
        harmonize = "Opportunistic harmonization (names, style) when editing"
        gaps = "Light review"

    linked_main = rel[:-9] + ".java" if rel.endswith("Test.java") else ""

    return (
        "test",
        rel,
        linked_main,
        scope,
        junit,
        str(tests),
        str(proto),
        str(fixme),
        str(ign),
        str(println),
        status,
        harmonize,
        gaps,
    )


def analyze_main_file(path: str, test_file_set: set[str]) -> tuple[str, ...]:
    rel = os.path.relpath(path, ROOT_MAIN).replace(os.sep, "/")
    linked_test = rel[:-5] + "Test.java"
    has_test = linked_test in test_file_set
    junit = infer_junit_from_test(os.path.join(ROOT_TEST, linked_test)) if has_test else "none"

    return (
        "main_class",
        rel,
        linked_test if has_test else "",
        "unit" if has_test else "missing_test",
        junit,
        "0",
        "0",
        "0",
        "0",
        "0",
        "main_with_dedicated_test" if has_test else "main_without_dedicated_test",
        "Keep JUnit5 for new tests" if not has_test else "Improve coverage depth if needed",
        "Add dedicated JUnit5 test class" if not has_test else "Increase scenario coverage where relevant",
    )


def main() -> None:
    test_rows = []
    test_files = set()

    for dirpath, _, filenames in os.walk(ROOT_TEST):
        for fn in sorted(filenames):
            if not fn.endswith("Test.java"):
                continue
            full = os.path.join(dirpath, fn)
            rel = os.path.relpath(full, ROOT_TEST).replace(os.sep, "/")
            test_files.add(rel)
            test_rows.append(analyze_test_file(full))

    main_rows = []
    for dirpath, _, filenames in os.walk(ROOT_MAIN):
        for fn in sorted(filenames):
            if not fn.endswith(".java"):
                continue
            full = os.path.join(dirpath, fn)
            main_rows.append(analyze_main_file(full, test_files))

    test_rows.sort(key=lambda r: (r[3] != "functional", r[1]))
    main_rows.sort(key=lambda r: r[1])

    rows = test_rows + main_rows

    header = [
        "entry_type",
        "file",
        "linked_class_or_test",
        "scope",
        "junit",
        "nb_tests",
        "nb_prototype_stubs",
        "nb_fixme_test",
        "nb_ignore",
        "nb_system_out",
        "status",
        "harmonize",
        "gaps_or_actions",
    ]
    with open(OUT, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f, delimiter=";", quoting=csv.QUOTE_MINIMAL)
        w.writerow(header)
        w.writerows(rows)
    print(f"Wrote {len(rows)} rows to {OUT}")


if __name__ == "__main__":
    main()
