# Parameters
param (
    [string]$fromVersion,
    [string]$latestVersion
)

$fromPath = "..\..\..\..\..\..\..\..\..\"
$latestPath = "..\..\..\" 
$logFile = Join-Path $fromPath "logs\update_log_${fromVersion}_${latestVersion}.txt"

# Functions

function Get-RelativePath {
    param (
        [string]$file,
        [string]$relativeTo
    )

    $fileFullPath = (Get-Item $file).FullName
    $relativeToFullPath = (Get-Item $relativeTo).FullName

    if ($fileFullPath -eq $relativeToFullPath) {
        return ""
    }

    if ($fileFullPath.StartsWith($relativeToFullPath)) {
        $relativePath = $fileFullPath.Substring($relativeToFullPath.Length)
        return $relativePath
    }

    throw "The file $file is not located within the directory $relativeTo."
}

function Copy-Files {
    param (
        [string]$relativePath,
        [bool]$overwrite
    )

    $source = Join-Path $latestPath $relativePath
    $destination = Join-Path $fromPath $relativePath

    if (-not (Test-Path $source)) {
        Log-Message "Source path $source not found. Skipping."
        return
    }

    Log-Message "Copying: $source -> $destination (overwrite=$overwrite)"

    if ((Get-Item $source).PSIsContainer) {
        # Source is a directory
        $files = Get-ChildItem -Path $source -File -Recurse
        foreach ($file in $files) {
            $relativeFilePath = Get-RelativePath -file $file.FullName -relativeTo $source
            $fileDestination = Join-Path $destination $relativeFilePath
            Copy-File $($file.FullName) $fileDestination
        }
    }
    else {
        Copy-File $source $destination
    }
}

function Copy-File {
    param (
        [string]$source,
        [string]$destination
    )

    if (-not $overwrite -and (Test-Path $destination)) {
        Log-Message "Skipped copying $source to $destination (overwrite=false)"
    }
    else {
        $destinationFolder = Split-Path $destination
        if (-not (Test-Path $destinationFolder)) {
            Log-Message "Creating destination folder: $destinationFolder"
            New-Item -ItemType Directory -Path $destinationFolder | Out-Null
        }
        Log-Message "Copying $source to $destination"
        Copy-Item -Path $source -Destination $destination -Force -ErrorAction SilentlyContinue 2>&1 | Tee-Object -FilePath $logFile -Append
    }
}

function Remove-Files {
    param (
        [string]$relativePath
    )

    if ($relativePath -ne $null -and $relativePath -ne '') {
        $destination = Join-Path $fromPath $relativePath
        if (-not (Test-Path $destination)) {
            Log-Message "Path $destination not found. Skipping remove."
            return
        }
        Log-Message "Removing $destination"
        Remove-Item -Recurse $destination -ErrorAction SilentlyContinue 2>&1 | Tee-Object -FilePath $logFile -Append
    }
}

function Countdown-And-Exit {
    param (
        [int]$seconds
    )

    $originalBackgroundColor = $host.UI.RawUI.BackgroundColor
    $originalForegroundColor = $host.UI.RawUI.ForegroundColor

    try {
        while ($seconds -gt 0) {
            $countdownMessage = "Exiting in $seconds seconds. Press any key to abort."
            Write-Host -NoNewline $countdownMessage

            Start-Sleep -Seconds 1

            if ([console]::KeyAvailable) {
                $key = [console]::ReadKey($true)
                if ($key.Key -ne "NoName") {
                    Write-Host "`nPress any key to exit." -ForegroundColor Yellow
                    $key = [console]::ReadKey($true)  # Wait for another key press
                    return
                }
            }

            $seconds--
            Write-Host "`r" -NoNewline
        }
    }
    finally {
        $host.UI.RawUI.BackgroundColor = $originalBackgroundColor
        $host.UI.RawUI.ForegroundColor = $originalForegroundColor
    }
}

function Log-Message {
    param (
        [string]$message
    )

    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    "$timestamp - $message" | Tee-Object -FilePath $logFile -Append
}

# Main script
Set-Location (Split-Path -Parent $MyInvocation.MyCommand.Path)
Log-Message "Working folder: $(Get-Location)"

Log-Message "-----------------------------------------------------"
Log-Message "--------------------- JaMuz -------------------------"
Log-Message "-----------------------------------------------------"
Log-Message "Starting update from $fromVersion to $latestVersion"
Log-Message "-----------------------------------------------------"

# Run one CSV per version step in (fromVersion, latestVersion], like DB migrations
$csvFiles = Get-ChildItem -Path "update_*.csv" -ErrorAction SilentlyContinue
$versionStrings = $csvFiles | ForEach-Object {
    if ($_.Name -match '^update_(.+)\.csv$') { $matches[1] }
}
$versionsToRun = $versionStrings | Sort-Object { try { [version]($_.Replace('v', '') -replace '[^\d.]', '') } catch { $_.ToString() } }

foreach ($v in $versionsToRun) {
    try {
        $fromV = [version]($fromVersion.Replace('v', '') -replace '[^\d.]', '')
        $toV = [version]($latestVersion.Replace('v', '') -replace '[^\d.]', '')
        $curV = [version]($v.Replace('-', '.') -replace '[^\d.]', '')
    } catch {
        continue
    }
    if ($curV -gt $fromV -and $curV -le $toV) {
        $csv = "update_$v.csv"
        if (-not (Test-Path -LiteralPath $csv -PathType Leaf)) {
            Log-Message "No $csv (skipping, same as empty)"
            continue
        }
        Log-Message "Applying migration $csv (version $v)"
        Get-Content $csv | ForEach-Object {
            $line = $_.Trim()
            if ($line -eq '') { return }
            $fields = $line -split ','
            $operation = $fields[0].Trim()
            $relativePath = $fields[1].Trim()
            if ($fields.Count -ge 3) {
                $overwrite = [bool]::Parse($fields[2].Trim())
            } else {
                $overwrite = $false
            }
            switch ($operation) {
                "Copy" { Copy-Files $relativePath $overwrite }
                "Remove" { Remove-Files $relativePath }
            }
        }
    }
}

$sourceJar = Join-Path $latestPath "JaMuz.jar"
$destinationJar = Join-Path $fromPath "JaMuz.jar"

Log-Message "Copying the new JAR file"
Copy-Item -Force $sourceJar $destinationJar 2>&1 | Tee-Object -FilePath $logFile -Append

Log-Message "Starting JaMuz"
Start-Process "java" -ArgumentList "-jar", $destinationJar -WindowStyle Hidden
Log-Message "JaMuz started"

Countdown-And-Exit 15
