---
layout: default
title: Latest Release Redirect
---

If you are not redirected, [click here](#){: onclick="getLatestRelease(); return false;" }.

<script>
async function getLatestRelease() {
    const apiUrl = 'https://api.github.com/repos/phramusca/JaMuz/releases/latest';
    const response = await fetch(apiUrl);
    const data = await response.json();
    const assetName = data.assets[0].name; // Assuming the asset you want is the first one
    const downloadUrl = `https://github.com/phramusca/JaMuz/releases/latest/download/${assetName}`;
    window.location.replace(downloadUrl);
}
</script>
