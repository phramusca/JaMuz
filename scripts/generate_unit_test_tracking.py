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
        etat, harm, manque = "hors_perimetre_unitaire", "Suivi separe (plan fonctionnel / TestPlan)", "—"
    elif proto >= tests and tests > 0:
        etat = "bloque_squelettes"
        harm = (
            "Remplacer squelettes NetBeans par tests reels ou supprimer; "
            "noms explicites (should…When…)"
        )
        manque = "Definir comportements attendus (DAO/API)"
    elif proto > 0:
        etat = "mixte_squelettes"
        harm = "Eliminer squelettes restants; reduire println; noms explicites"
        manque = "Cas negatifs + contraintes (voir FIXME dans fichier)"
    elif fixme > 0:
        etat = "a_completer_fixme"
        harm = (
            "Traiter FIXME; choisir alignement JUnit (ce fichier seul en JUnit 5)"
            if junit == "junit5"
            else "Traiter FIXME; noms should…When…; AAA si gros blocs"
        )
        manque = "Lire commentaires FIXME TEST dans le fichier"
    elif println > 8:
        etat = "revue_logs"
        harm = "Remplacer System.out par assertions ou logs de test"
        manque = "Verifier stabilite / determinisme"
    else:
        etat = "plutot_propre"
        harm = "Harmonisation opportuniste (noms, style) si retouche"
        manque = "Revue legere"

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
