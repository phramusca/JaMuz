#!/bin/bash

readme() {
   echo ''
   echo 'Usage:'
   echo ''
   echo '   sh ./release.sh <version> <backup path>'
   echo ''
   echo ' <version> must be of type x.y.z (ex: 0.5.41)'
   echo ' <backup path> is the path to the releases backup folder'
   echo '   Ex: 1.0.51'
   echo '   Regex: ^[0-9]+\.[0-9]+\.[0-9]+$'
   echo ''
}

echo "-------------"
echo "JaMuz Release"
echo "-------------"
echo""

version=$1
backupPath=$2
echo "Requested version: $version"
if (echo "$version" | grep -Eq  ^[0-9]+\.[0-9]+\.[0-9]+$);
    then
        echo "Version is valid."
    else
        echo ""
        echo "Error: version \"$version\" does not match required pattern! "
        readme
        exit
fi
echo "Requested path: $backupPath"
if [ -d $backupPath ];
    then
        echo "Path exists on your filesystem."
    else
        echo ""
        echo "Error: \"$backupPath\" does not exists on your filesystem!"
        readme
        exit
fi
echo ""
read -r -p "Continue with the release ? [Y/n] " input
 case $input in
      [yY][eE][sS]|[yY])
            cp -r dist ../../ReposSides/JaMuz/Backup_Releases/
            mv ../../ReposSides/JaMuz/Backup_Releases/dist ../../ReposSides/JaMuz/Backup_Releases/JaMuz_v${version}_Beta
            7z a -r ../../ReposSides/JaMuz/Backup_Releases/JaMuz_v${version}_Beta.7z ../../ReposSides/JaMuz/Backup_Releases/JaMuz_v${version}_Beta
            rm -rf ../../ReposSides/JaMuz/Backup_Releases/dist ../../ReposSides/JaMuz/Backup_Releases/JaMuz_v${version}_Beta
            ;;
      [nN][oO]|[nN])
            echo "Operation cancelled."
            ;;
      *)
            echo "Invalid input..."
            exit 1
            ;;
esac
echo "End."