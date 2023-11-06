@echo off

REM Check if Chocolatey is installed
choco --version > nul 2>&1
if %errorlevel% neq 0 (
  echo Installing Chocolatey package manager...
  @powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"
  if %errorlevel% neq 0 (
    echo Error: Failed to install Chocolatey. Please install it manually.
    pause
    exit /b 1
  )
)

REM Install SQLite3 using Chocolatey
echo Installing SQLite3 using Chocolatey...
choco install sqlite -y
if %errorlevel% neq 0 (
  echo Error: Failed to install SQLite3. Please check your Chocolatey installation.
  pause
  exit /b 1
)

REM Set the working directory to the script's directory
cd %~dp0

REM Set the desired database name
set DB_NAME=JaMuz.db

REM List of SQL files to execute
set SQL_FILES=JaMuz_creation.sql JaMuz_insertion_minimal.sql JaMuz_insertion_optional.sql

REM Loop through the list of SQL files and execute them
for %%F in (%SQL_FILES%) do (
  echo Executing SQL file: %%F
  sqlite3 "%DB_NAME%" < %%F
  echo SQL file executed.
)

pause
