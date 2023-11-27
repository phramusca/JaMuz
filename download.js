// Add this to your download.js file
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

        // Create an invisible link
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = assetName;
        link.textContent = `Download latest version ${version}`;

        // Specify the location on the page to append the link
        const downloadContainer = document.getElementById(repo.containerId);
        if (downloadContainer) {
            downloadContainer.appendChild(link);
        } else {
            document.body.appendChild(link);
        }
    }
});

// async function getLatestRelease(repoName) {
//     const apiUrl = `https://api.github.com/repos/${repoName}/releases/latest`;
//     const response = await fetch(apiUrl);
//     const data = await response.json();
//     const assetName = data.assets[0].name;
//     const downloadUrl = `https://github.com/${repoName}/releases/latest/download/${assetName}`;
    
//     // Create an invisible link
//     const link = document.createElement('a');
//     link.href = downloadUrl;
//     link.download = assetName;

//     // Append the link to the document and trigger a click
//     document.body.appendChild(link);
//     link.click();

//     // Remove the link from the document
//     document.body.removeChild(link);
// }
