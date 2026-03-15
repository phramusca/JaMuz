#!/bin/bash

#Parameters

fromVersion=$1
fromPath="../../../../../../../../../"
latestVersion=$2
latestPath="../../../"
# Normalize for version comparison (strip leading 'v')
fromVer="${fromVersion#v}"
latestVer="${latestVersion#v}"

logFile="${fromPath%/}/logs/update_log_${fromVersion}_${latestVersion}.txt"

# Functions

copy_files() {
  local relativePath="$1"
  local overwrite="$2"

  local source="${latestPath%/}/${relativePath}"
  local destination="${fromPath%/}/${relativePath}"

  if [ ! -e "$source" ]; then
    log_message "Source path $source not found. Skipping."
    return
  fi

  log_message "Copying: $source -> $destination (overwrite=$overwrite)"

  destination="${destination%/}"
  destination="${destination%/*}" # Go up to parent

  mkdir -p "$destination"

  if [ "$overwrite" = true ]; then
    log_message "Copying (overwrite) $source to $destination"
    cp -r "$source" "$destination" 2>&1 | tee -a "$logFile"
  else
    log_message "Copying $source to $destination"
    cp -rn "$source" "$destination" 2>&1 | tee -a "$logFile"
  fi
}

remove_files() {
  local relativePath="$1"

  if [ -n "$relativePath" ]; then
    local destination="${fromPath%/}/${relativePath}"
    if [ ! -e "$destination" ]; then
      log_message "Path $destination not found. Skipping remove."
      return
    fi
    log_message "Removing $destination"
    rm -r "$destination" 2>&1 | tee -a "$logFile"
  fi
}

countdown_and_exit() {
    local seconds=$1

    while [ $seconds -gt 0 ]; do
        echo -ne "Exiting in $seconds seconds. Press any key to abort.\033[0K\r"
        if read -t 1 -s -n 1; then
            echo -ne "\033[2K\rPress any key to exit."
            read -s -n 1
            return
        fi
        ((seconds--))
    done
}

log_message() {
  local message="$1"
  echo "$(date '+%Y-%m-%d %H:%M:%S') - $message" | tee -a "$logFile"
}

# Main script

log_message "-----------------------------------------------------"
log_message "--------------------- JaMuz -------------------------"
log_message "-----------------------------------------------------"
log_message "Starting update from $fromVersion to $latestVersion"
log_message "-----------------------------------------------------"
cd "$(dirname "$(readlink -f "$0")")"
log_message "Working folder: $(pwd)"

# Run one CSV per version step in (fromVersion, latestVersion], like DB migrations
# Collect versions from update_X.Y.Z.csv, sort, then run each in range in order
for csv in update_*.csv; do
  [ -f "$csv" ] || continue
  v="${csv#update_}"
  echo "${v%.csv}"
done | sort -V | while read -r v; do
  # Run this version if fromVer < v <= latestVer
  sorted_after_from=$(printf '%s\n' "$fromVer" "$v" | sort -V | tail -1)
  sorted_before_latest=$(printf '%s\n' "$v" "$latestVer" | sort -V | head -1)
  if [ "$sorted_after_from" = "$v" ] && [ "$sorted_before_latest" = "$v" ]; then
    csv="update_${v}.csv"
    log_message "Applying migration $csv (version $v)"
    while IFS=, read -r operation relativePath overwrite || [ -n "$operation" ]; do
      case "$operation" in
      "Copy") copy_files "$relativePath" "$overwrite" ;;
      "Remove") remove_files "$relativePath" ;;
      esac
    done <"$csv"
  fi
done

log_message "Copying the new JAR file"
cp -f "${latestPath}JaMuz.jar" "${fromPath}JaMuz.jar" 2>&1 | tee -a "$logFile"

log_message "Restarting JaMuz"
nohup java -jar "${fromPath}JaMuz.jar" >/dev/null 2>&1 &
log_message "JaMuz restarted"

countdown_and_exit 15
