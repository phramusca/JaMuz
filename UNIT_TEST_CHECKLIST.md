# Tests unitaires JaMuz — guide et suivi

Ce document est volontairement **court et structuré**. Le détail **fichier par fichier** est dans le CSV.

---

## Fichiers déjà traités (squelettes → vrais tests)

À mettre à jour à chaque lot. Les entrées **code** indiquent un correctif dans `src/main` lié au fichier de test.

| Fichier test | Notes |
|--------------|--------|
| `jamuz/database/DaoClientWriteTest.java` | + correctif `DaoClientWrite` (`setNull` pour FK device/source) |
| `jamuz/database/DaoDeviceFileWriteTest.java` | |
| `jamuz/database/DaoDeviceWriteTest.java` | |
| `jamuz/database/DaoFileTagWriteTest.java` | |
| `jamuz/database/DaoFileTranscodedWriteTest.java` | |
| `jamuz/database/DaoFileWriteTest.java` | |
| `jamuz/database/DaoGenreWriteTest.java` | + correctif `DaoGenre.isSupported` (`ResultSet.next`) |

*Dernière mise à jour de ce tableau : 2026-04-27.*

---

## 1. Fichiers à utiliser

| Fichier | Rôle |
|--------|------|
| **`UNIT_TEST_TRACKING.csv`** | Suivi : **une ligne par** `*Test.java` sous `src/test/java` (70 fichiers au scan). Colonnes automatiques + colonnes « harmoniser » / « manques ». |
| **`UNIT_TEST_CHECKLIST.md`** (ce fichier) | Comment lire le CSV, lexique des états, synthèse **globale** (harmonisation / manques). |

**Régénérer le CSV** (compteurs et états à jour), depuis le répertoire `JaMuz/` :

```bash
python3 scripts/generate_unit_test_tracking.py
```

---

## 2. Colonnes du CSV (`UNIT_TEST_TRACKING.csv`)

Séparateur **point-virgule** (`;`) pour ouvrir confortablement dans LibreOffice / Excel (FR).

| Colonne | Signification |
|--------|----------------|
| `fichier` | Chemin relatif à `src/test/java/`. |
| `perimetre` | `unitaire` ou `fonctionnel` (`tests/functional/`). |
| `junit` | `junit4`, `junit5`, ou `inconnu` (imports atypiques). |
| `nb_tests` | Méthodes annotées `@Test` (JUnit 4 ou Jupiter). |
| `nb_squelettes_prototype` | Lignes contenant `fail("The test case is a prototype.")` (génération NetBeans non remplie). |
| `nb_fixme_test` | Occurrences de `FIXME TEST` dans le fichier. |
| `nb_ignore` | Occurrences de `@Ignore`. |
| `nb_system_out` | Occurrences de `System.out.println`. |
| `etat_synthese` | Résumé machine lisible (voir §3). |
| `harmoniser` | Piste d’harmonisation **pour ce fichier**. |
| `manques_ou_actions` | Piste **couverture / TODO** pour ce fichier. |

---

## 3. Lexique : `etat_synthese`

| Valeur | Signification |
|--------|----------------|
| `hors_perimetre_unitaire` | Sous `tests/functional/` : ne fait pas partie du plan « unitaires seuls » ; suivi à part. |
| `bloque_squelettes` | Toutes (ou quasi) les méthodes `@Test` sont encore des prototypes : **priorité** remplacer ou supprimer. |
| `mixte_squelettes` | Au moins un test réel, mais il reste des squelettes : nettoyer pour que `mvn test` reflète l’état réel. |
| `a_completer_fixme` | Peu ou pas de squelettes, mais des `FIXME TEST` à traiter. |
| `revue_logs` | Beaucoup de `println` : bruit et risque pour la CI / la lecture des rapports. |
| `plutot_propre` | Pas de signal fort automatique ; revue légère si tu touches au module. |

---

## 4. Synthèse globale (analyse existant)

### 4.1 Ce qui existe aujourd’hui

- **Emplacement** : `JaMuz/src/test/java/`, surtout `jamuz.database` (gros volume), puis `jamuz.utils`, `jamuz.soulseek`, `jamuz` (racine), `jamuz.process`, et **`tests/functional`** (hors périmètre unitaire dans le CSV).
- **Exécution Maven** : Surefire n’inclut que `**/*Test.java` (`pom.xml`). Les autres classes d’aide ne partent pas avec `mvn test` par défaut.
- **JUnit** : presque tout en **JUnit 4** ; **une** classe en **JUnit 5** : `jamuz/FileInfoIntTest.java` — à trancher pour harmonisation projet-wide.
- **Constat chiffré** (snapshot au moment de la génération du CSV) : **~177** squelettes `prototype` répartis surtout sur `jamuz.database` ; détail exact par fichier = colonne `nb_squelettes_prototype` dans le CSV.

### 4.2 Harmoniser (thèmes transverses)

1. **Squelettes NetBeans** : remplacer par un test minimal utile, ou supprimer / `@Ignore` avec une **raison** en une ligne (évite des `mvn test` rouges inutiles).
2. **Nommage** : viser des noms de méthode **explicites** (ex. `shouldRejectWhenLoginNull`) ; renommage **au fil des fichiers** modifiés.
3. **`System.out.println`** : réduire dans les tests (surtout `database`, `utils`) au profit d’assertions ou d’un logger configurable.
4. **JUnit 4 vs 5** : soit ramener `FileInfoIntTest` en JUnit 4, soit planifier une montée progressive vers Jupiter — **une décision** suffit pour éviter le mélange long terme.
5. **`jamuz.utils`** : beaucoup de tests **sensibles à l’OS / réseau / UI** ; les traiter comme **intégration** ou les garder mais les **taguer / documenter** dans le CSV (colonne `harmoniser` fichier par fichier).

### 4.3 Manques (thèmes transverses)

1. **DAO / DB** : cas **négatifs**, **contraintes** SQL, et méthodes non couvertes — souvent déjà signalés par `FIXME TEST` dans le fichier (colonne `nb_fixme_test`).
2. **Modèles** : `FileInfoIntTest` — getters/setters / chemins signalés dans les FIXME.
3. **Helpers de test** : ex. `TestProcessHelper` (pas un `*Test.java` : **absent du CSV** ; à suivre à part ou renommer en `*Test` si tu veux l’inclure à Surefire).

---

## 5. Ordre de travail suggéré (tout est à faire à terme)

1. `jamuz/database/*Test.java` — plus gros impact (squelettes + FIXME).  
2. `jamuz/FileInfoIntTest.java` + `FileInfoTest.java`.  
3. `jamuz/soulseek/*Test.java` — souvent déjà `plutot_propre` (vérifier dans le CSV).  
4. `jamuz/utils/*Test.java` — clarifier unitaire vs intégration.  
5. `tests/functional` — plan séparé.

Pour le **détail par fichier**, ouvre **`UNIT_TEST_TRACKING.csv`** et trie / filtre sur `etat_synthese` ou `perimetre`.
