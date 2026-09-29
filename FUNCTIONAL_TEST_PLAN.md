# Tests fonctionnels JaMuz

Les tests fonctionnels valident les processus **Check**, **Merge** et **Sync** de bout en bout, avec GUI Swing, base SQLite de test et fichiers audio dans `testenv/`. Pour les conventions détaillées de diagnostic, modification des fixtures et ajout de scénarios, voir le skill [`jamuz-functional-tests`](.github/skills/jamuz-functional-tests/SKILL.md).

## Lancement

- **IDE recommandé** : [NetBeans](https://netbeans.apache.org/) (historiquement utilisé pour l'UI et ces tests)
- **Alternative** : Cursor / VS Code avec configuration JUnit 5
- **Répertoire de travail** : racine du module `JaMuz/` (obligatoire — les chemins `testenv/` sont relatifs à `.`)
- **Commande Maven ciblée** : `mvn -Dtest=tests.functional.CheckNTest test`
- **Vérification des fixtures** : `mvn -Dtest=test.helpers.FixtureYamlTest test`
- **Les tests fonctionnels ont besoin d'un environnement graphique** (GUI Swing). NetBeans peut exécuter une classe via JUnit ; Maven est préférable pour la commande reproductible ci-dessus.

Classes :

| Classe | Scénario |
|---|---|
| `tests.functional.Merge1Test` | Merge simple (1 album, 1 source) |
| `tests.functional.CheckNTest` | Check complet (8 albums) |
| `tests.functional.MergeNTest` | Merge multi-sources (8 albums, 4 sources) |
| `tests.functional.MergeCheckNTest` | Merge + Check + Sync (scénario complet) |

Prérequis :

- `testenv/Ressources/` :
  - base minimale (`JaMuz_Minimal.db`), copiée puis migrée automatiquement au schéma courant au démarrage du test
  - fixtures d'albums YAML sous `albumFiles_yaml/<suite>/<scénario>/<releaseId>.yaml`
  - bases stat sources
- Les scripts de migration de schéma sont copiés automatiquement depuis `target-data/data/system/sql` vers `testenv/AppFolder/data/system/sql` avant la configuration de l'application. Les scripts présents sous `target-data-local/data/system/sql` sont copiés ensuite et remplacent les fichiers de même nom.
- `testenv/Musique/` et `testenv/StatSources/` : recréés à l'exécution (gitignored)
- Affichage graphique disponible (GUI Swing)
- Les recherches MusicBrainz et Last.fm sont externes et peuvent rallonger ou faire varier certains résultats ; voir les règles ci-dessous avant d'actualiser une attente.

## Données de référence (YAML)

Les fixtures sont décrites dans `testenv/Ressources/albumFiles_yaml/<suite>/<scénario>/<releaseId>.yaml`. Chaque fichier définit l'état attendu des pistes, des métadonnées, des résultats de contrôle et du contexte d'exécution à une étape du scénario.

Ordre canonique des sections : `formatVersion`, `releaseId`, `scenario`, puis `process`, `expectedResults`, `tracks`. Les sections de taille fixe restent en premier ; les listes de pistes, de taille variable, restent à la fin.

- `process` mémorise notamment l'index de scan, l'ID DB et l'emplacement. Pour `CheckNTest`, l'ordre de scan est vérifié explicitement dans `FixtureYamlTest` ; ne pas déduire ou modifier ces index sans contrôler l'ordre réellement observé.
- `expectedResults.<clé>.status` vaut `ok`, `warning` ou `ko`. Une valeur attendue vide signifie que le comparateur ne vérifie pas ce résultat. Les chaînes `{N/A}` et `{IGNORE}` ne sont pas des jokers.
- `tracks` est une liste ordonnée : chaque entrée correspond à un fichier audio du scénario. Les champs sont des chaînes, à l'exception de l'indicateur facultatif `ignore` qui est booléen. Conserver les zéros initiaux des numéros de piste et l'ordre utilisé par l'album.
- Un snapshot de scénario doit être copié/mis à jour pour chaque étape pertinente (`CheckTest1_KO`, `CheckTest2_OK`, `CheckTest3_DbOk`) ; ne modifier les fixtures `REFERENCE` ou d'autres suites que si elles font explicitement partie de la demande.

Les suites utilisent les IDs suivants. `CheckNTest` utilise désormais la release
`fb9dd163-1d07-4dea-a0e3-779845d94167` sélectionnée par la recherche MusicBrainz ;
`9e097b10-8160-491e-a310-e26e54a86a10` reste utilisé par les scénarios de merge.

- `fb9dd163-1d07-4dea-a0e3-779845d94167` (CheckNTest)
- `9e097b10-8160-491e-a310-e26e54a86a10`
- `9dc7fe6a-3fa4-4461-8975-ecb7218b39a3`
- `c212b71b-848c-491c-8ae7-b62a993ae194`
- `8cfbb741-bd63-449f-9e48-4d234264c8d5`
- `be04bc1f-fc63-48f5-b1ca-2723f17d241d`
- `6cc35892-c44f-4aa7-bfee-5f63eca70821`
- `7598d527-bc8d-4282-a72c-874f335d05ac`
- `13ca98f6-1a9f-4d76-a3b3-a72a16d91916`

## Scénarios

### CheckNTest

1. Créer albums `CheckTest1_KO` dans `Nouveau/`
2. Scan nouveau dossier → vérifier file d'actions
3. Pour chaque album : genre Reggae + cover → action SAVE → `applyChanges`
4. Re-scan → état `CheckTest2_OK` (OK auto)
5. `applyChanges` → scan vide → état `CheckTest3_DbOk` en DB et FS

Les assertions doivent rester strictes. Le comparateur de `Album` groupe les différences de pistes et de résultats par album ; `CheckNTest` collecte ensuite les erreurs par album et par phase avant de terminer sur un `assertAll`. Cela permet une seule exécution de diagnostic plutôt qu'une boucle « première assertion → correction → relance ». La collecte ne doit pas masquer une erreur qui empêche la suite du scénario de s'exécuter (ex. nombre d'albums scannés inattendu ou panne d'application).

Une exécution complète récente de `CheckNTest` a pris environ 3 min 20 s, principalement avec les consultations externes. Corriger tous les écarts issus du même rapport avant de relancer.

#### Actualiser une attente après un écart

1. Séparer les écarts par release, phase, piste et champ ; regrouper les différences communes (casse, année, valeur par défaut) sans remplacer toute la fixture.
2. Distinguer changement légitime des données externes et régression JaMuz. Pour une release choisie différemment par les règles de score/date, vérifier la sélection actuelle puis modifier l'ID et ses trois snapshots ensemble ; ne pas modifier l'algorithme de sélection pour faire passer le test.
3. Confirmer le fournisseur du match avant de toucher à l'année : les résultats Last.fm peuvent ne pas avoir d'année, ce qui donne un avertissement cohérent. Ne pas confondre les releases `6cc…` et `c212…`.
4. Les comparaisons étant exactes, traiter ponctuation Unicode, apostrophes typographiques, casse, accents, crédits invités, numéro/total de disque, indicateur de vérification et note comme des différences réelles jusqu'à explication.
5. Après mise à jour, lancer ensemble `CheckNTest` et `FixtureYamlTest`; examiner le résultat complet, pas seulement la première erreur.
6. Vérifier les effets secondaires de test. Les tests peuvent réécrire l'horodatage du commentaire initial de `testenv/AppFolder/JaMuz.properties`. Restaurer uniquement cette ligne si elle a été changée par l'exécution, sans écraser d'autres modifications. Ne supprimer que des fichiers générés identifiés comme appartenant à l'exécution.

### Merge1Test

1. Créer `MergeTest1_Creation` → scan rapide → `MergeTest2_DB`
2. Merge 1ère fois → `MergeTest3` / `MergeTest4_*`
3. Modifier stats JaMuz → 2e merge → `MergeTest5_*`

### MergeNTest

Étapes `MergeDevice1_KO` → `MergeDevice6_New` :

1. Création + scan rapide
2. 1er merge (stats sources → JaMuz)
3. Modification stats dans chaque source + JaMuz (`MergeDevice5_*`)
4. 2e merge → synchronisation `MergeDevice6_New`

Sources statistiques (Linux) :

| idStatement | Source | Fichier DB |
|---|---|---|
| 1 | Guayadeque | `guayadeque_Device.db` |
| 2 | Kodi | `MyMusic32_Device.db` |
| 4 | Mixxx | `mixxxdb_Device.sqlite` |
| 5 | MyTunes (Android, avec device) | `MusicIndexDatabase_Device.db` |

### MergeCheckNTest

Reprend le scénario MergeN puis enchaîne :

1. Check library → SAVE (genre/cover) → OK → `MergeDevice7_KO` → `MergeDevice9_DbOk`
2. 3e merge (fichiers introuvables attendus) → modif stats MyTunes + JaMuz
3. 4e merge → sync device → mise à jour bases sources
4. 5e et 6e merge → `MergeDevice11_Sync2` (toutes sources synchronisées)

Références détaillées : `testenv/Ressources/albumFiles_MergeCheckNTest-RawMaterialToStart/`

## Hors périmètre (reporté)

- MediaMonkey (Windows, idStatement 3)
- Stat sources sur SSH ou FTP
- Un device par stat source (un seul device MyTunes suffit au scénario actuel)
- Tests Robot Framework (`robot/`) — couche GUI distincte
- Exécution CI automatique (nécessite runner graphique)

## Helpers et données

- `test.helpers.TestSettings` — configuration app + GUI
- `test.helpers.TestProcessHelper` — orchestration check/merge/sync
- `test.helpers.AlbumBuffer` / `Album` — chargement de fixtures YAML, création des fichiers audio et assertions
- `test.helpers.FixtureYaml` — parsing sécurisé et validation structurelle des fixtures
- `test.helpers.FixtureYamlTest` — validation de l'inventaire, de la forme YAML, des nombres de fixtures et des invariants `CheckNTest`

Les anciens fichiers ODS de données fonctionnelles ont été remplacés par YAML. Ne pas réintroduire de lecture ODS pour ces fixtures ; le code de compatibilité ODS restant peut encore servir à d'autres fonctions/tests et doit être vérifié avant suppression.
