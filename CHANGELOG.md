# JaMuz change log #

## v0.7.0 ##

### What's new ? ###

- Version check and auto-update
- Jamuz Android sync:
  - API 2.0 : add model as name (called by Jamuz Android since v0.6.6)
  - New option to activate new clients by default
  - Combobox to select IP
  - Send current playlist name to remote
- Dependencies check (metaflac, mp3gain, mplayer) on linux (as windows executables are already included in package)
- Soulseek support (using [slskd](https://github.com/slskd/slskd). Also, please refer to Soulseek's [rules](https://www.slsknet.org/news/node/681))
- Keyboard shortcuts documented in options panel (though present since the beginning)
- Translation updates
- Various fixes
- Moved to maven
- New release process with github actions

### Package content ###

| Path                 | Incl. | Description                                                                                       |
| -------------------- | ----- | ------------------------------------------------------------------------------------------------- |
| /data/cache          | No    | Cache folder. You can remove it, files will be re-created.                                        |
| /data/icon/genre     | Yes   | Genre icons. You can add more.                                                                    |
| /data/icon/tag       | Yes   | Tag icons. You can add more.                                                                      |
| /data/system         | Yes   | System files. You should not touch this.                                                          |
| /data/AudioLinks.txt | Yes   | You can edit links to your favorite audio information providers.                                  |
| /data/BookLinks.txt  | Yes   | You can edit links to your favorite book information providers.                                   |
| /data/VideoLinks.txt | Yes   | You can edit links to your favorite video information providers.                                  |
| /data/Patterns.txt   | No    | Saved patterns for music file scanner. If you want to clean it up.                                |
| /doc                 | Yes   | Includes sample JaMuz.xml.                                                                        |
| /logs                | No    | LOG files (and databases backups).                                                                |
| ***/JaMuz.db***      | Yes   | ***Your new music library (back it up regularly !)***.                                            |
| ***/JaMuz.jar***     | Yes   | ***Program itself*** (*on linux, set execution permission*).                                      |
| /JaMuz.properties    | Yes   | Configuration file (avoid manual edition).                                                        |
| /JaMuz.xml           | No    | Configuration file (optional). Used to setup your database location. See /doc/JaMuz.xml template. |
| /myMovieDb.db        | Yes   | Database for Video tab.                                                                           |
| /Slsk.properties     | Yes   | Configuration file for slskd (avoid manual edition).                                              |

***Compatible with JaMuz Remote v0.5.x minimum***

## v0.6.0 ##

***Compatible with JaMuz Remote v0.5.x minimum***

### What's new ? ###

- Contextual menus reviewed and fixed
- Client edition menu reviewed and fixed
- Player info panel reviewed and fixed
- Various bug fixes
- Cover hash SHA-256
- Compatible with JaMuz Remote v0.5.x minimum
- Fix default options: NO MORE auto convertions (wma:mp3,ogg:mp3,m4a:mp3,mpc:mp3) and files deletion (db,ini,txt,m3u,pls,htm,html,doc,nfo,url,sfv,wpl,sfk)

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.sh         | Linux launch script                                   |
| /myMovieDb.db     | Database fro Video tab                                |
| /JaMuz.xml        | Configuration file (optional). See template in /doc   |

## v0.5.1 ##

***Compatible with JaMuz Remote v0.5.x minimum***

### What's new ? ###

- Various fixes
- Translations updates
- Database updates handled. Support from 0.5.0

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.sh         | Linux launch script                                   |
| /myMovieDb.db     | Database fro Video tab                                |
| /JaMuz.xml        | Configuration file (optional). See template in /doc   |

## v0.5.0-beta ##

***Compatible with JaMuz Remote v0.5.x minimum***

### What's new ? ###

- Improved Check process:
  - Improved duplicates handling:
  - Ability to replace a duplicate
  - Buttons "None are duplicates" "Not a duplicate"
    - Apply file pattern to KO too
    - Shrink covers to 1000x1000 pixels
    - Button to set disc and track numbers
- Improved Remote Sync:
  - New sync process using java-express
    - Full library synced and ability to download track(s) from JaMuz Remote
    - Ability to transcode to MP3
- Popup menu in playlists (with new items)
  - AcoustId (Alpha)
- CopyRight filter
- Various bug fixes
- New translation: Portuguese. Others updated.
- Compatible with JaMuz Remote v0.5.x minimum

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.sh         | Linux launch script                                   |
| /myMovieDb.db     | Database fro Video tab                                |
| /JaMuz.xml        | Configuration file (optional). See template in /doc   |

## v0.4.1-beta ##

***Compatible with JaMuz Remote v0.4.x only***

### What's new ? ###

- Better duplicate searching
- Auto set number of check processes based on available threads.
- Scanner: WordUtils (::C ::l ...) + legend
- Sync user tags with guayadeque and mixxx
- Support kodi profiles in source options
- FLAC support by default
- Various and many fixes
- Remove MB wait (good idea ?)

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.sh         | Linux launch script                                   |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.xml        | Configuration file (optional)                         |

## v0.4.0-beta ##

***Compatible with JaMuz Remote v0.4.x only***

### What's new ? ###

- Sync improved and easy setup
- New translation (Portuguese (Brazil))

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.sh         | Linux launch script                                   |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.xml        | Configuration file (optional)                         |

## v0.3.0-beta ##

***Compatible with JaMuz Remote v0.3.x only***

### What's new ? ###

- Log cleanup feature
- Sync fixes
- New and improved translations

### Package content ###

| Path              | Description                                           |
| ----------------- | ----------------------------------------------------- |
| /data             | Data files (configurable)                             |
| /doc              | Documentation                                         |
| /lib              | Required libraries                                    |
| /logs             | LOG files (and databases backups)                     |
| ***/JaMuz.db***   | ***Your new music library (back it up regularly !)*** |
| /JaMuz.jar        | Program itself                                        |
| /JaMuz.sh         | Linux launch script                                   |
| /JaMuz.properties | Configuration file                                    |
| /JaMuz.xml        | Configuration file (optional)                         |

## v0.2.2-beta ##

***Compatible with JaMuz Remote v0.2.x only***

### What's new ? ###

- Remote: merge progress in table
- Video: cleanup feature, renaming fixed, stable
- Books: fixes, stable

## v0.2.1-beta ##

***Compatible with JaMuz Remote v0.2.x only***

### What's new ? ###

- Move merge with JaMuz Remote to Remote panel.

## v0.2.0-beta ##

***Compatible with JaMuz Remote v0.2.x only***

### What's new ? ###

- Lot of changes.

## v0.1.0-beta ##

**Deprecated**

2nd release, first beta version

## v0.0.24-alpha ##

**Deprecated**

- Alpha
- 1st released version

## Previous releases, before github ##

### 0.0.22 ###

- Check: multi-thread + bug fix

### 0.0.21 ###

- Raccourcis clavier (MultiRemote). Check actions. Video: support notes séries. Débuggé sync et merge.

### 0.0.20 ###

- Vidéo: support séries télé, liens externes, filtres. Check: MANUAL action. Lyrics editor.

### 0.0.19 ###

- Ajout support TheMovieDb dans Videos + divers

### 0.0.18 ###

- Ajout test junit, stabilisation merge, video, stats tabs. Removed Best Of tab

### 0.0.17 ###

- Check: analyse auto. Video: fini (ou presque)

### 0.0.16 ###

- Correction problème de merge avec appareil quand chemin changé dans JaMuz

### 0.0.15 ###

- Debug lecteur et améliorations partie vidéo

### 0.0.14 ###

- Améliorations onglet Choisir

### 0.0.13 ###

- Amélioration sélecteur pochettes (onglet Vérifier)

### 0.0.12 ###

- Qq trucs, à lister

### 0.0.11 ###

- Qq trucs, à lister, voir VERSION.txt

### 0.0.10 ###

- Onglet choisir: Album coloré selon le check ET menu popup

### 0.0.9 ###

- Bug fix

### 0.0.8 ###

- Qq trucs, à lister

### 0.0.7 ###

- Amélioration vitesse copie pour synchro vers appareil (batch base de données)

### 0.0.6 ###

- Ajout IHM pour affichage plein écran sur 2ème écran, et autres améliorations

### 0.0.5 ###

- Correction problème vérification dossiers

### 0.0.4 ###

- Finalisation suport playlist et device, vérification albums, et autres améliorations

### 0.0.3 ###

- Ajout synchro vers appareil et nombreuses améliorations

### 0.0.2 ###

- Amélioration du Check, notamment ajout du "Check library"

### 0.0.1 ###

- Première version avec le numéro de version dans le titre (MANIFEST)

### 0.0.0 ###

- Première version (sans numéro)
