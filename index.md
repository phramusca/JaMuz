---
layout: default
title: Your Page Title
# js_include: ./download.js
---

# Welcome to JaMuz

| JaMuz Android                                                                                                                                                                                                                                | JaMuz Desktop                                                                           |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
| [<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="60">](https://f-droid.org/packages/org.phramusca.jamuz/)<BR/>or [get latest apk](https://github.com/phramusca/JaMuz-Remote/releases/latest) | [Latest release 7z package](#){: onclick="getLatestRelease(); return false;" } |

## 

![French](img/flag_france.png) [Version française](index_fr.md)

![English](img/flag_england.png) [English version](index_en.md)

## [Wiki (Help)](https://github.com/phramusca/JaMuz/wiki)

- I'll always be pleased if you offer me a beer (or a cup of tea, or more) to support my contribution :)
  - Recurrent donations using Liberapay.com: <a href="https://liberapay.com/phramusca/donate"><img alt="Donate using Liberapay" src="https://liberapay.com/assets/widgets/donate.svg"></a>
  - One time donation with PayPal.com: <a href="https://paypal.me/RaphaelCamus"><img alt="Donate using PayPal" src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif"></a>

<script src="./download.js"></script>

<!-- <script>
  async function getLatestRelease() {
    const apiUrl = 'https://api.github.com/repos/phramusca/JaMuz/releases/latest';
    const response = await fetch(apiUrl);
    const data = await response.json();
    const assetName = data.assets[0].name; // Assuming the asset you want is the first one
    const downloadUrl = `https://github.com/phramusca/JaMuz/releases/latest/download/${assetName}`;

    // Create an invisible link
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.download = assetName;

    // Append the link to the document and trigger a click
    document.body.appendChild(link);
    link.click();

    // Remove the link from the document
    document.body.removeChild(link);
}
</script> -->
