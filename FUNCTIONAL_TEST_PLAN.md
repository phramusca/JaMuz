# Plan des tests fonctionnels JaMuz

Les tests fonctionnels valident les processus **Check**, **Merge** et **Sync** de bout en bout, avec GUI Swing, base SQLite réelle et fichiers audio dans `testenv/`.

## Lancement

- **IDE recommandé** : [NetBeans](https://netbeans.apache.org/) (historiquement utilisé pour l'UI et ces tests)
- **Alternative** : Cursor / VS Code avec configuration JUnit 4
- **Répertoire de travail** : racine du module `JaMuz/` (obligatoire — les chemins `testenv/` sont relatifs à `.`)
- **Commande** : lancer une classe de test individuellement depuis l'IDE (pas via `mvn test`)

Classes :

| Classe | Scénario |
|---|---|
| `tests.functional.Merge1Test` | Merge simple (1 album, 1 source) |
| `tests.functional.CheckNTest` | Check complet (8 albums) |
| `tests.functional.MergeNTest` | Merge multi-sources (8 albums, 4 sources) |
| `tests.functional.MergeCheckNTest` | Merge + Check + Sync (scénario complet) |

Prérequis :

- `testenv/Ressources/` : base minimale, fichiers ODS album, bases stat sources
- `testenv/Musique/` et `testenv/StatSources/` : recréés à l'exécution (gitignored)
- Affichage graphique disponible (GUI Swing)

## Données de référence (ODS)

Chaque album MusicBrainz est décrit dans `testenv/Ressources/albumFiles/<mbId>.ods`. Chaque feuille nommée (ex. `MergeDevice5_JaMuz`) définit l'état attendu des pistes et métadonnées à une étape du scénario.

8 albums utilisés :

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

## Helpers

- `test.helpers.TestSettings` — configuration app + GUI
- `test.helpers.TestProcessHelper` — orchestration check/merge/sync
- `test.helpers.AlbumBuffer` / `Album` — création albums et assertions ODS
