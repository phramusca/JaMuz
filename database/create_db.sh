#!/bin/bash

# Check if SQLite3 is installed
if ! command -v sqlite3 &>/dev/null; then
  echo "SQLite3 is not installed. Installing SQLite3..."
  
  # Check the package manager and install SQLite3 accordingly
  if command -v apt-get &>/dev/null; then
    sudo apt-get update
    sudo apt-get install sqlite3 -y
  elif command -v yum &>/dev/null; then
    sudo yum update
    sudo yum install sqlite -y
  else
    echo "Error: Unsupported package manager. Please install SQLite3 manually."
    exit 1
  fi

  # Check if the installation was successful
  if ! command -v sqlite3 &>/dev/null; then
    echo "Error: Failed to install SQLite3. Please check your package manager or install it manually."
    exit 1
  else
    echo "SQLite3 installed successfully."
  fi
fi

# Set the desired database name
DB_NAME=JaMuz.db

# List of SQL files to execute
SQL_FILES=("JaMuz_creation.sql" "JaMuz_insertion_minimal.sql" "JaMuz_insertion_optional.sql")

# Loop through the list of SQL files and execute them
for SQL_FILE in "${SQL_FILES[@]}"; do
  echo "Executing SQL file: $SQL_FILE"
  sqlite3 "$DB_NAME" < "$SQL_FILE"
  echo "SQL file executed."
done
