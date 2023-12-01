---
layout: default
---

<!-- Slick Carousel CSS -->
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"/>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css"/>

<style>
  .carouselImage {
    margin:0 auto;
  }
</style>

<!-- jQuery and Slick Carousel JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>

<!-- Initialize Slick Carousel -->
<script type="text/javascript">
  $(document).ready(function(){
    // JaMuz Android
    var numberOfImages = 15;
    var carouselAndroid = $('.carousel-android');
    for (var i = 1; i <= numberOfImages; i++) {
      var imageUrl = 'img/phoneScreenshots/' + i + '.png';
      carouselAndroid.append('<div><img src="' + imageUrl + '" alt="Image" class="carouselImage"></div>');
    }
    carouselAndroid.slick({
      dots: true,
      infinite: true,
      autoplay: true,
      speed: 500,
      fade: true,
      cssEase: 'linear'
    });

    // JaMuz Desktop
    var images = [
      'img/desktop/Choisir.png',
      'img/desktop/Fusionner.png',
      'img/desktop/Exporter.png',
      'img/desktop/Verifier.png',
      'img/desktop/Listes.png',
      'img/desktop/Slsk.png',
      'img/desktop/Stats.png',
      'img/desktop/Serveur.png',
      'img/desktop/Options.png',
    ];
    var carouselDesktop = $('.carousel-desktop');
      images.forEach(function(imageUrl) {
      carouselDesktop.append('<div><img src="' + imageUrl + '" alt="Image" class="carouselImage"></div>');
    });
    carouselDesktop.slick({
        dots: true,
        infinite: true,
        autoplay: true,
        speed: 500,
        fade: true,
        cssEase: 'linear'
      });
    });
</script>

<script src="./download.js"></script>

|                         | JaMuz Android                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | JaMuz Desktop                                                                                                                 |
| ----------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| Platform(s)             | <img src="img/android.png" alt="JaMuz Android">                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | <img src="img/linux.png" alt="Linux"> <img src="img/windows.png" alt="Windows"> <img src="img/raspberry.png" alt="Raspberry"> |
| Overview                | ![JaMuz Android](img/phoneScreenshots/1.png)                                                                                                                                                                                                                                                                                                                                                                                                                                                        | ![JaMuz Desktop](img/desktop/Choisir.png)                                                                                                                       |
| Download & installation | [<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/org.phramusca.jamuz/)<br>- [Get F-Droid](https://f-droid.org/F-Droid.apk) and [install it](https://www.androidauthority.com/how-to-install-apks-31494/).<br>- Open F-Droid, search for `JaMuz` and install it.<br><br>OR <span id="download-container-jamuz-android"></span> and [install it](https://www.androidauthority.com/how-to-install-apks-31494/), but you will not get updates! | - <span id="download-container-jamuz-desktop"></span><br>- Extract 7z archive<br>- Double-click on `JaMuz.jar`                |
| Available in            | <img src="https://hosted.weblate.org/widgets/jamuz-remote/-/translations/multi-auto.svg" alt="Translation status">                                                                                                                                                                                                                                                                                                                                                                                                            | <img src="https://hosted.weblate.org/widgets/jamuz/-/translations/multi-auto.svg" alt="Translation status">                   |

``JaMuz Android`` and ``JaMuz Desktop`` can be used independently but it is recommended to use both together.

## JaMuz Android

<div class="carousel-android"></div>

- Audio Player:
  - Playlist editor (user tags, rating and genre)
  - Tag editor (user tags, rating and genre)
  - Replaygain support (MP3 only, FLAC on its way)
  - One finger control
  - Voice commands
- Merge your statistics with [JaMuz Desktop](https://github.com/phramusca/JaMuz)
  - And so with all players JaMuz Desktop can merge with (Mixxx, Kodi, guayadeque,...)
- Sync files from [JaMuz Desktop](https://github.com/phramusca/JaMuz) over WiFi
  - Select a playlist on JaMuz, connect JaMuz Remote sync, and wait for your files.
- Remote control for [JaMuz Desktop](https://github.com/phramusca/JaMuz)

## JaMuz Desktop

A **music** library **manager**, **player** and more, for **Linux**, **Raspberry** and **Windows**.

<div class="carousel-desktop"></div>

### Features

#### Sync data between audio players

**Sync** your tracks' **rating**, **custom tags**, **BPM**, **genre** and statistics (**last played**, **play counter** and **added date**) between JaMuz Desktop and your favorite players:

- [Guayadeque](https://doc.ubuntu-fr.org/guayadeque) (Linux)
- [Kodi](https://kodi.tv/) (Linux / Windows)
- [Media Monkey](https://www.mediamonkey.com/) (Windows)
- [Mixxx](https://mixxx.org/) (Linux / Windows)
- [JaMuz Android](https://github.com/phramusca/JaMuz-Remote)
- ... please [contribute](CONTRIBUTING.md) to add some more.

#### Sync files to devices

- Select a playlist and a destination (Phone, USB Key/HDD, MP3 player, ...).
- Process deletes unwanted files on destination then copy new files.

#### Sync with [JaMuz Android](https://github.com/phramusca/JaMuz-Remote)

- Using [JaMuz Android](https://github.com/phramusca/JaMuz-Remote), you can combine both above features, but **over WiFi**.
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

## Donate

I'll always be pleased if you offer me a beer (or a cup of tea, or more) to support my contribution :)

<a href="https://liberapay.com/phramusca/donate"><img alt="Donate using Liberapay" src="https://liberapay.com/assets/widgets/donate.svg"></a>
