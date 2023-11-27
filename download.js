document.addEventListener("DOMContentLoaded", async function () {
    const repositories = [
        { repoName: "phramusca/JaMuz", containerId: "download-container-jamuz-desktop" },
        { repoName: "phramusca/JaMuz-Remote", containerId: "download-container-jamuz-android" }
    ];

    for (const repo of repositories) {
        const apiUrl = `https://api.github.com/repos/${repo.repoName}/releases/latest`;
        const response = await fetch(apiUrl);
        const data = await response.json();
        const assetName = data.assets[0].name;
        const downloadUrl = `https://github.com/${repo.repoName}/releases/latest/download/${assetName}`;
        const version = data.tag_name;

        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = assetName;
        link.textContent = `Download latest version ${version} (${repo.repoName})`;

        // Specify the location on the page to append the link
        const downloadContainer = document.getElementById(repo.containerId);
        if (downloadContainer) {
            downloadContainer.appendChild(link);
        } else {
            // If the specified container doesn't exist, append the link to the body
            document.body.appendChild(link);
        }
    }
});
