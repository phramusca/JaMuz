PRAGMA foreign_keys=off;
ALTER TABLE file RENAME TO _file_old;
ALTER TABLE path RENAME TO _path_old;
ALTER TABLE tagfile RENAME TO _tagfile_old;
ALTER TABLE playCounter RENAME TO _playCounter_old;
ALTER TABLE device RENAME TO _device_old;
ALTER TABLE deviceFile RENAME TO _deviceFile_old;
ALTER TABLE client RENAME TO _client_old;
ALTER TABLE statSource RENAME TO _statSource_old;

CREATE TABLE IF NOT EXISTS "file" (
	"idFile"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"idPath"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"rating"	INTEGER NOT NULL,
	"lastPlayed"	TEXT NOT NULL,
	"playCounter"	INTEGER NOT NULL,
	"addedDate"	TEXT NOT NULL,
	"artist"	TEXT NOT NULL,
	"album"	TEXT NOT NULL,
	"albumArtist"	TEXT NOT NULL,
	"title"	TEXT NOT NULL,
	"trackNo"	INTEGER NOT NULL,
	"trackTotal"	INTEGER NOT NULL,
	"discNo"	INTEGER NOT NULL,
	"discTotal"	INTEGER NOT NULL,
	"genre"	TEXT NOT NULL,
	"year"	TEXT NOT NULL,
	"BPM"	REAL NOT NULL,
	"comment"	TEXT NOT NULL,
	"nbCovers"	INTEGER NOT NULL,
	"bitRate"	TEXT NOT NULL,
	"format"	TEXT NOT NULL,
	"length"	INTEGER NOT NULL,
	"size"	INTEGER NOT NULL,
	"modifDate"	TEXT NOT NULL,
	"coverHash"	TEXT NOT NULL,
	"ratingModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"tagsModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"genreModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"saved"	INTEGER NOT NULL DEFAULT (0),
	"trackGain"	REAL,
	"albumGain"	REAL,
	FOREIGN KEY("idPath") REFERENCES "path"("idPath") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "tagfile" (
	"idFile"	INTEGER NOT NULL,
	"idTag"	INTEGER NOT NULL,
	FOREIGN KEY("idTag") REFERENCES "tag"("id") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile") ON DELETE CASCADE,
	PRIMARY KEY("idFile","idTag")
);

CREATE TABLE IF NOT EXISTS "playCounter" (
	"idFile"	INTEGER NOT NULL,
	"idStatSource"	INTEGER NOT NULL,
	"playCounter"	INTEGER NOT NULL,
	FOREIGN KEY("idStatSource") REFERENCES "statsource"("idStatSource") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile") ON DELETE CASCADE,
	PRIMARY KEY("idFile","idStatSource")
);

CREATE TABLE IF NOT EXISTS "device" (
	"idDevice"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"source"	TEXT NOT NULL,
	"destination"	TEXT NOT NULL,
	"idPlaylist"	INTEGER,
	"idMachine"	INTEGER NOT NULL,
	FOREIGN KEY("idMachine") REFERENCES "machine"("idMachine") ON DELETE CASCADE,
	FOREIGN KEY("idPlaylist") REFERENCES "playlist"("idPlaylist")
);

CREATE TABLE IF NOT EXISTS "deviceFile" (
	"idFile"	INTEGER NOT NULL,
	"idDevice"	INTEGER NOT NULL,
	"oriRelativeFullPath"	TEXT NOT NULL,
	"status"	TEXT NOT NULL,
	PRIMARY KEY("idFile","idDevice"),
	FOREIGN KEY("idDevice") REFERENCES "device"("idDevice") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "client" (
	"idClient"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"login"	TEXT NOT NULL UNIQUE,
	"pwd"	TEXT NOT NULL,
	"name"	TEXT NOT NULL UNIQUE,
	"idDevice"	INTEGER,
	"idStatSource"	INTEGER,
	"enabled"	BOOL,
	FOREIGN KEY("idDevice") REFERENCES "device"("idDevice") ON DELETE CASCADE,
	FOREIGN KEY("idStatSource") REFERENCES "statSource"("idStatSource") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "statSource" (
	"idStatSource"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"location"	TEXT NOT NULL,
	"idStatement"	INTEGER NOT NULL,
	"rootPath"	TEXT NOT NULL,
	"idMachine"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"idDevice"	INTEGER,
	"selected"	INTEGER NOT NULL,
	"lastMergeDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	FOREIGN KEY("idDevice") REFERENCES "device"("idDevice"),
	FOREIGN KEY("idMachine") REFERENCES "machine"("idMachine") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "path" (
	"idPath"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"strPath"	TEXT NOT NULL UNIQUE,
	"modifDate"	TEXT NOT NULL,
	"checked"	INTEGER NOT NULL DEFAULT (0),
	"copyRight"	INTEGER NOT NULL DEFAULT (0),
	"mbId"	TEXT
);

INSERT INTO file SELECT idFile,	idPath,	name, rating, lastPlayed, playCounter, addedDate, 
artist, album, albumArtist, title, trackNo, trackTotal, discNo, discTotal, genre,
year, BPM, comment, nbCovers, bitRate, format, length, size, modifDate, coverHash, ratingModifDate,
tagsModifDate, genreModifDate, saved, trackGain, albumGain FROM _file_old;
INSERT INTO path SELECT idPath, strPath, modifDate, checked, copyRight, mbId FROM _path_old;

INSERT INTO tagfile SELECT * FROM _tagfile_old;
INSERT INTO playCounter SELECT * FROM _playCounter_old;
INSERT INTO device SELECT * FROM _device_old;
INSERT INTO deviceFile SELECT * FROM _deviceFile_old;
INSERT INTO client SELECT * FROM _client_old;
INSERT INTO statSource SELECT * FROM _statSource_old;

DROP TABLE _file_old;
DROP TABLE _path_old;
DROP TABLE _tagfile_old;
DROP TABLE _playCounter_old;
DROP TABLE _device_old;
DROP TABLE _deviceFile_old;
DROP TABLE _client_old;
DROP TABLE _statSource_old;

PRAGMA foreign_keys=on;