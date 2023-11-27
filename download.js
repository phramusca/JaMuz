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