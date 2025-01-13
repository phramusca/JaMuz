---
layout: default
---

<!-- Slick Carousel CSS -->
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"/>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css"/>

<style>
  .carousel-android-image {
    margin:0 auto;
    height: 400px;
    width: auto;
  }

  .carousel-desktop-image {
    margin:0 auto;
    width: 600px;
    height: auto;
  }
</style>

<!-- jQuery and Slick Carousel JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>

<!-- Initialize Slick Carousel -->
<script src="./scripts/carousel.js"></script>

<!-- Initialize download links -->
<script src="./scripts/download.js"></script>

| [JaMuz Desktop](#jamuz-desktop) | [JaMuz Android](#jamuz-android) |
|--|--|
| <img src="img/linux.png" alt="Linux"> <img src="img/windows.png" alt="Windows"> <img src="img/raspberry.png" alt="Raspberry"> | <img src="img/android.png" alt="JaMuz Android">  |
| Manage, play and serve your music collection to JaMuz Android. | Play, tag, rate and remote control JaMuz Desktop. |

## JaMuz Android

An Android **Audio Player** and also a **Remote** for [JaMuz Desktop](#jamuz-desktop).

<div class="carousel-android"></div>

### Installation <img src="img/android.png" alt="JaMuz Android"> 

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/org.phramusca.jamuz/)

- [Get F-Droid](https://f-droid.org/F-Droid.apk) and [install it](https://www.androidauthority.com/how-to-install-apks-31494/).
- Open F-Droid, search for `JaMuz` and install it.

OR <span id="download-container-jamuz-android"></span> and [install it](https://www.androidauthority.com/how-to-install-apks-31494/), but you will not get updates!

### Features

- Audio Player:
  - Playlist editor (user tags, rating and genre)
  - Tag editor (user tags, rating and genre)
  - Replaygain support (MP3 and FLAC, if from JaMuz Desktop)
  - One finger control
  - [Voice commands](https://github.com/phramusca/JaMuz-Remote/blob/master/data/voiceCommands.md)
- Merge your statistics with [JaMuz Desktop](#jamuz-desktop)
  - And so with all players JaMuz Desktop can merge with (Mixxx, Kodi, guayadeque,...)
- Sync files from [JaMuz Desktop](#jamuz-desktop) over WiFi
  - Select a playlist on JaMuz, connect JaMuz Remote sync, and wait for your files.
- Remote control for [JaMuz Desktop](#jamuz-desktop)

### International

JaMuz Android is available in the following languages:

<img src="https://hosted.weblate.org/widgets/jamuz-remote/-/translations/multi-auto.svg" alt="Translation status">

Please help out translating by using [WebLate](https://hosted.weblate.org/engage/jamuz-remote/).

## JaMuz Desktop

A **music** library **manager**, **player** and more, for **Linux**, **Raspberry** and **Windows**.

<div class="carousel-desktop"></div>

### Installation

#### Linux <img src="img/linux.png" alt="Linux">

- Install Java (OpenJDK JRE) >=17
  - Debian based (Ubuntu, Mint,...): [openjdk-17-jre](apt://openjdk-17-jre)
  - Any other case, refer to [Adoptium](https://adoptium.net/installation/)
- <span id="download-container-jamuz-desktop"></span>
- Extract 7z archive
- Double-click on `JaMuz.jar`

#### Windows <img src="img/windows.png" alt="Windows">

- Install Java (OpenJDK JRE) >=17: 
  - [Eclipse Temurin OpenJDK17U-jre_x64_windows](https://adoptium.net/fr/temurin/releases/?os=windows&arch=x64&package=jre&version=17) (Pick .msi)
- <span id="download-container-jamuz-desktop"></span>
- Extract 7z archive
  - Note: 7z is natively supported since October, 31, 2023 on Windows 11 too (with update [KB5031455](https://support.microsoft.com/en-us/topic/october-31-2023-kb5031455-os-builds-22621-2506-and-22631-2506-preview-6513c5ec-c5a2-4aaf-97f5-44c13d29e0d4)). Otherwise, you need to [install 7-zip](https://www.7-zip.org/).
- Double-click on `JaMuz.jar`

#### Raspberry <img src="img/raspberry.png" alt="Raspberry">

- Install Java (OpenJDK JRE) >=17
  - [Deb installation on Debian or Ubuntu](https://adoptium.net/installation/linux/#_deb_installation_on_debian_or_ubuntu), or in brief:
    ```bash
    sudo apt install -y wget apt-transport-https gpg
    sudo wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
    sudo echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | sudo tee /etc/apt/sources.list.d/adoptium.list
    sudo apt update
    sudo apt install temurin-17-jre
    ```
- <span id="download-container-jamuz-desktop"></span>
- Extract 7z archive
- Manually install dependencies (as raspberry does not support apt:// by default):
    ```bash
    sudo apt install mplayer
    sudo apt install flac
    sudo apt install mp3gain
    ```
- Double-click on `JaMuz.jar`

### Features

#### Sync data between audio players

**Sync** your tracks' **rating**, **custom tags**, **BPM**, **genre** and statistics (**last played**, **play counter** and **added date**) between JaMuz Desktop and your favorite players:

- [Guayadeque](https://doc.ubuntu-fr.org/guayadeque) (Linux)
- [Kodi](https://kodi.tv/) (Linux / Windows)
- [Media Monkey](https://www.mediamonkey.com/) (Windows)
- [Mixxx](https://mixxx.org/) (Linux / Windows)
- **[JaMuz Android](#jamuz-android)**
- ... please [contribute](CONTRIBUTING.md) to add some more.

#### Sync files to devices

- Select a playlist and a destination (Phone, USB Key/HDD, MP3 player, ...).
- Process deletes unwanted files on destination then copy new files.

#### Sync with [JaMuz Android](#jamuz-android)

- Using [JaMuz Android](#jamuz-android), you can combine both above features, but **over WiFi**.
- You can also **remote control** JaMuz Desktop audio player.

#### Manage, Organize and Convert

- **Get** tracks **metadata** and **covers** from [MusicBrainz](https://musicbrainz.org/), [last.fm](https://www.last.fm/) and [Cover Art Archive](https://coverartarchive.org/).
- Detect **duplicates**.
- Compute **ReplayGain** (MP3 and FLAC).
- **Convert** files using [JAVE (Java Audio Video Encoder)](http://www.sauronsoftware.it/projects/jave/)
- **Rename** files and folders with tracks metadata and a mask (ex: "%albumartist%/%album%/%track% %title%").

#### And also

- Create complex **playlists** in a simple way.
- Audio **player** (using mplayer).
- Library statistics with graphs.
- Lyrics display.
- Runs on **Windows, Raspberry and Linux** (as written in Java), maybe Mac OS (not tested).
- Bonus:
  - Kodi helper to manage your videos.
  - Calibre helper to manage your ebooks.

### International

JaMuz Desktop is available in the following languages:

<a href="https://hosted.weblate.org/engage/jamuz/?utm_source=widget">
<img src="https://hosted.weblate.org/widgets/jamuz/-/translations/multi-auto.svg" alt="Translation status" />
</a>

Please help out translating by using [WebLate](https://hosted.weblate.org/engage/jamuz/).

## Donate

I'll always be pleased if you give me a little something to support my contribution :)

<a href="https://liberapay.com/phramusca/donate"><img alt="Donate using Liberapay" src="https://liberapay.com/assets/widgets/donate.svg"></a>
