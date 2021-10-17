**********************************************************
********************           ***************************
********************   JaMuz   ***************************
********************           ***************************
**********************************************************

----------------------------------------------------------------------------------------------------------------------------------
Path				Description
----------------------------------------------------------------------------------------------------------------------------------
/data				Data files (configurable)
/doc				Documentation
/lib				Required libraries
/logs				LOG files (and databases backups)
/JaMuz.db		    Your new music library (back it up regularly !)
/JaMuz.jar		    Program itself
/JaMuz.properties	Configuration file
/JaMuz.sh		    Linux launch script
/myMovieDb.db		Database fro Video tab

/JaMuz.xml			Configuration file (optional)
						to change path and name of the music library
						location. See template in /doc
----------------------------------------------------------------------------------------------------------------------------------

History
----------------------------------------------------------------------------------------------------------------------------------
0.5.0	Beta	- Improved Check process:
					- Improved duplicates handling:
						- Ability to replace a duplicate
						- Buttons "None are duplicates" "Not a duplicate"
					- Apply file pattern to KO too
					- Shrink covers to 1000x1000 pixels
					- Button to set disc and track numbers
				- Improved Remote Sync:
					- New sync process using java-express
					- Full library synced and ability to download track(s) from JaMuz Remote
					- Ability to transcode to MP3
				- Popup menu in playlists (with new items)
					- AcoustId (Alpha)
				- CopyRight filter
				- Various bug fixes
				- New translation: Portuguese. Others updated.
				- Compatible with JaMuz Remote v0.5.x only
----------------------------------------------------------------------------------------------------------------------------------
0.4.1	Beta	- Better duplicate searching
                - Auto set number of check processes based on available threads.
                - Scanner: WordUtils (::C ::l ...) + legend
                - Sync user tags with guayadeque and mixxx
                - Support kodi profiles in source options
                - FLAC support by default
                - Various and many fixes
                - Remove MB wait (good idea ?)
				- Compatible with JaMuz Remote v0.4.x only
----------------------------------------------------------------------------------------------------------------------------------
0.4.0	Beta	- Sync improved and easy setup
				- New translation (Portuguese (Brazil))
				- Compatible with JaMuz Remote v0.4.x only
----------------------------------------------------------------------------------------------------------------------------------
0.3.0	Beta	- Log cleanup feature
				- Sync fixes
				- New and improved translations
				- Compatible with JaMuz Remote v0.3.x only
----------------------------------------------------------------------------------------------------------------------------------
0.2.2	Beta	- Remote: 	merge progress in table
				- Video: 	cleanup feature, renaming fixed, stable
				- Livres: 	fixes, stable
				- Compatible with JaMuz Remote v0.2.x only
----------------------------------------------------------------------------------------------------------------------------------
0.2.1   Beta    - Move merge with JaMuz Remote to Remote panel
				- Compatible with JaMuz Remote v0.2.x only
----------------------------------------------------------------------------------------------------------------------------------
0.2.0	Beta	- Lot of changes. 
				- Compatible with JaMuz Remote v0.2.x only
----------------------------------------------------------------------------------------------------------------------------------
0.1.0	Beta	- 2nd release
				- First beta version
----------------------------------------------------------------------------------------------------------------------------------
0.0.24	Alpha	- 1st released version
----------------------------------------------------------------------------------------------------------------------------------
