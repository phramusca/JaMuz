BEGIN TRANSACTION;
DROP TABLE IF EXISTS "machine";
CREATE TABLE IF NOT EXISTS "machine" (
	"idMachine"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL UNIQUE,
	"description"	TEXT,
	"hidden"	INTEGER NOT NULL
);
DROP TABLE IF EXISTS "client";
CREATE TABLE IF NOT EXISTS "client" (
	"idClient"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"login"	TEXT NOT NULL UNIQUE,
	"pwd"	TEXT NOT NULL,
	"name"	TEXT NOT NULL,
	"idDevice"	INTEGER,
	"idStatSource"	INTEGER,
	"enabled"	BOOL,
	FOREIGN KEY("idDevice") REFERENCES "device"("idDevice") ON DELETE CASCADE,
	FOREIGN KEY("idStatSource") REFERENCES "statSource"("idStatSource") ON DELETE CASCADE
);
DROP TABLE IF EXISTS "file";
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
	"deleted"	INTEGER NOT NULL,
	"coverHash"	TEXT NOT NULL,
	"ratingModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"tagsModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"genreModifDate"	TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	"saved"	INTEGER NOT NULL DEFAULT (0),
	"trackGain"	REAL,
	"albumGain"	REAL,
	FOREIGN KEY("idPath") REFERENCES "path"("idPath")
);
DROP TABLE IF EXISTS "fileTranscoded";
CREATE TABLE IF NOT EXISTS "fileTranscoded" (
	"idFile"	INTEGER NOT NULL,
	"ext"	TEXT NOT NULL,
	"bitRate"	TEXT NOT NULL,
	"format"	TEXT NOT NULL,
	"length"	INTEGER NOT NULL,
	"size"	INTEGER NOT NULL,
	"trackGain"	REAL,
	"albumGain"	REAL,
	"modifDate"	TEXT NOT NULL,
	PRIMARY KEY("idFile","ext")
);
DROP TABLE IF EXISTS "playlist";
CREATE TABLE IF NOT EXISTS "playlist" (
	"idPlaylist"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL UNIQUE,
	"limitDo"	INTEGER NOT NULL,
	"limitValue"	INTEGER NOT NULL,
	"limitUnit"	TEXT NOT NULL,
	"type"	TEXT NOT NULL,
	"match"	TEXT NOT NULL,
	"random"	INTEGER NOT NULL,
	"hidden"	INTEGER NOT NULL,
	"destExt"	TEXT NOT NULL
);
DROP TABLE IF EXISTS "tagfile";
CREATE TABLE IF NOT EXISTS "tagfile" (
	"idFile"	INTEGER NOT NULL,
	"idTag"	INTEGER NOT NULL,
	FOREIGN KEY("idTag") REFERENCES "tag"("id") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile"),
	PRIMARY KEY("idFile","idTag")
);
DROP TABLE IF EXISTS "tag";
CREATE TABLE IF NOT EXISTS "tag" (
	"id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"value"	TEXT NOT NULL UNIQUE
);
DROP TABLE IF EXISTS "path";
CREATE TABLE IF NOT EXISTS "path" (
	"idPath"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"strPath"	TEXT NOT NULL UNIQUE,
	"modifDate"	TEXT NOT NULL,
	"deleted"	INTEGER NOT NULL DEFAULT (0),
	"checked"	INTEGER NOT NULL DEFAULT (0),
	"copyRight"	INTEGER NOT NULL DEFAULT (0),
	"mbId"	TEXT
);
DROP TABLE IF EXISTS "statSource";
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
DROP TABLE IF EXISTS "playCounter";
CREATE TABLE IF NOT EXISTS "playCounter" (
	"idFile"	INTEGER NOT NULL,
	"idStatSource"	INTEGER NOT NULL,
	"playCounter"	INTEGER NOT NULL,
	FOREIGN KEY("idStatSource") REFERENCES "statsource"("idStatSource") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile"),
	PRIMARY KEY("idFile","idStatSource")
);
DROP TABLE IF EXISTS "playlistOrder";
CREATE TABLE IF NOT EXISTS "playlistOrder" (
	"idPlaylistOrder"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"desc"	INTEGER NOT NULL,
	"field"	TEXT NOT NULL,
	"idPlaylist"	INTEGER,
	FOREIGN KEY("idPlaylist") REFERENCES "playlist"("idPlaylist") ON DELETE CASCADE
);
DROP TABLE IF EXISTS "playlistFilter";
CREATE TABLE IF NOT EXISTS "playlistFilter" (
	"idPlaylistFilter"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"field"	TEXT NOT NULL,
	"operator"	TEXT NOT NULL,
	"value"	TEXT NOT NULL,
	"idPlaylist"	INTEGER,
	FOREIGN KEY("idPlaylist") REFERENCES "playlist"("idPlaylist") ON DELETE CASCADE
);
DROP TABLE IF EXISTS "option";
CREATE TABLE IF NOT EXISTS "option" (
	"idOption"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"idMachine"	INTEGER NOT NULL,
	"idOptionType"	INTEGER NOT NULL,
	"value"	TEXT NOT NULL,
	FOREIGN KEY("idMachine") REFERENCES "machine"("idMachine") ON DELETE CASCADE,
	FOREIGN KEY("idOptionType") REFERENCES "optiontype"("idOptionType")
);
DROP TABLE IF EXISTS "optionType";
CREATE TABLE IF NOT EXISTS "optionType" (
	"idOptionType"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"default"	TEXT NOT NULL,
	"type"	TEXT NOT NULL
);
DROP TABLE IF EXISTS "genre";
CREATE TABLE IF NOT EXISTS "genre" (
	"id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"value"	TEXT NOT NULL UNIQUE
);
DROP TABLE IF EXISTS "device";
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
DROP TABLE IF EXISTS "deviceFile";
CREATE TABLE IF NOT EXISTS "deviceFile" (
	"idFile"	INTEGER NOT NULL,
	"idDevice"	INTEGER NOT NULL,
	"oriRelativeFullPath"	TEXT NOT NULL,
	"status"	TEXT NOT NULL,
	PRIMARY KEY("idFile","idDevice"),
	FOREIGN KEY("idDevice") REFERENCES "device"("idDevice") ON DELETE CASCADE,
	FOREIGN KEY("idFile") REFERENCES "file"("idFile")
);
COMMIT;
