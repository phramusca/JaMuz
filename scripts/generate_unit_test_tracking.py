#!/usr/bin/env python3
"""Regenerate JaMuz/UNIT_TEST_TRACKING.csv from src/test/java/**/*Test.java."""
import csv
import os
import re

ROOT = os.path.join(os.path.dirname(__file__), "..", "src", "test", "java")
OUT = os.path.join(os.path.dirname(__file__), "..", "UNIT_TEST_TRACKING.csv")


def analyze_file(path: str) -> tuple[str, ...]:
    rel = os.path.relpath(path, ROOT).replace(os.sep, "/")
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
    junit = "junit5" if jup else ("junit4" if j4 else "inconnu")
    scope = "fonctionnel" if rel.startswith("tests/functional/") else "unitaire"

    if scope == "fonctionnel":
        etat, harm, manque = "hors_perimetre_unitaire", "Track separately (functional plan / TestPlan)", "—"
    elif proto >= tests and tests > 0:
        etat = "bloque_squelettes"
        harm = (
            "Replace NetBeans stubs with real tests or remove; "
            "explicit names (should…When…)"
        )
        manque = "Define expected behaviour (DAO/API)"
    elif proto > 0:
        etat = "mixte_squelettes"
        harm = "Remove remaining stubs; reduce println; explicit names"
        manque = "Negative cases + constraints (see FIXME in file)"
    elif fixme > 0:
        etat = "a_completer_fixme"
        harm = (
            "Address FIXME; pick JUnit alignment (this file alone is JUnit 5)"
            if junit == "junit5"
            else "Address FIXME; should…When… names; arrange-act-assert for large tests"
        )
        manque = "Read FIXME TEST comments in the file"
    elif println > 8:
        etat = "revue_logs"
        harm = "Replace System.out with assertions or test logging"
        manque = "Check stability / determinism"
    else:
        etat = "plutot_propre"
        harm = "Opportunistic harmonisation (names, style) when editing"
        manque = "Light review"

    return (
        rel,
        scope,
        junit,
        str(tests),
        str(proto),
        str(fixme),
        str(ign),
        str(println),
        etat,
        harm,
        manque,
    )


def main() -> None:
    rows = []
    for dirpath, _, filenames in os.walk(ROOT):
        for fn in sorted(filenames):
            if not fn.endswith("Test.java"):
                continue
            rows.append(analyze_file(os.path.join(dirpath, fn)))
    rows.sort(key=lambda r: (r[1] != "fonctionnel", r[0]))
    header = [
        "fichier",
        "perimetre",
        "junit",
        "nb_tests",
        "nb_squelettes_prototype",
        "nb_fixme_test",
        "nb_ignore",
        "nb_system_out",
        "etat_synthese",
        "harmoniser",
        "manques_ou_actions",
    ]
    with open(OUT, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f, delimiter=";", quoting=csv.QUOTE_MINIMAL)
        w.writerow(header)
        w.writerows(rows)
    print(f"Wrote {len(rows)} rows to {OUT}")


if __name__ == "__main__":
    main()
