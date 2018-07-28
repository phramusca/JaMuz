CREATE TABLE "device" (
    "idDevice" INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,
    "name" TEXT NOT NULL,
    "source" TEXT NOT NULL,
    "destination" TEXT NOT NULL,
    "idPlaylist" INTEGER,
    "idMachine" INTEGER NOT NULL,
    FOREIGN KEY(idPlaylist) REFERENCES playlist(idPlaylist),
    FOREIGN KEY(idMachine) REFERENCES machine(idMachine) ON DELETE CASCADE
);
CREATE TABLE "genre" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "value" TEXT NOT NULL
);
CREATE TABLE "optionType" (
    "idOptionType" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT NOT NULL,
    "default" TEXT NOT NULL,
    "type" TEXT NOT NULL
);
CREATE TABLE "option" (
    "idOption" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "idMachine" INTEGER NOT NULL,
    "idOptionType" INTEGER NOT NULL,
    "value" TEXT NOT NULL,
    FOREIGN KEY(idOptionType) REFERENCES optiontype(idOptionType),
	FOREIGN KEY(idMachine) REFERENCES machine(idMachine) ON DELETE CASCADE
);
CREATE TABLE "playlistFilter" (
	"idPlaylistFilter" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	"field" TEXT NOT NULL,
	"operator" TEXT NOT NULL,
	"value" TEXT NOT NULL,
	"idPlaylist" INTEGER,
	FOREIGN KEY(idPlaylist) REFERENCES playlist(idPlaylist) ON DELETE CASCADE
);
CREATE TABLE "playlistOrder" (
	"idPlaylistOrder" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	"desc" INTEGER NOT NULL,
	"field" TEXT NOT NULL,
	"idPlaylist" INTEGER,
	FOREIGN KEY(idPlaylist) REFERENCES playlist(idPlaylist) ON DELETE CASCADE
);
CREATE TABLE "playCounter" (
    "idFile" INTEGER NOT NULL,
    "idStatSource" INTEGER NOT NULL,
    "playCounter" INTEGER NOT NULL, 
	PRIMARY KEY ("idFile", "idStatSource"),
	FOREIGN KEY(idFile) REFERENCES file(idFile),
	FOREIGN KEY(idStatSource) REFERENCES statsource(idStatSource) ON DELETE CASCADE
);
CREATE TABLE "deviceFile" (
    "idFile" INTEGER NOT NULL,
    "idDevice" INTEGER NOT NULL,
    "oriRelativeFullPath" TEXT NOT NULL,
	PRIMARY KEY ("idFile", "idDevice", "oriRelativeFullPath"),
	FOREIGN KEY(idFile) REFERENCES file(idFile),
	FOREIGN KEY(idDevice) REFERENCES device(idDevice) ON DELETE CASCADE
);
CREATE TABLE "statSource" (
    "idStatSource" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "location" TEXT NOT NULL,
    "idStatement" INTEGER NOT NULL,
    "rootPath" TEXT NOT NULL,
    "idMachine" INTEGER NOT NULL,
    "name" TEXT NOT NULL,
    "idDevice" INTEGER,
    "selected" INTEGER NOT NULL, 
    "lastMergeDate" TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
	FOREIGN KEY(idMachine) REFERENCES machine(idMachine) ON DELETE CASCADE,
	FOREIGN KEY(idDevice) REFERENCES device(idDevice)
);
CREATE TABLE "path" (
    "idPath" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "strPath" TEXT NOT NULL,
    "modifDate" TEXT NOT NULL,
    "deleted" INTEGER NOT NULL DEFAULT (0),
    "checked" INTEGER NOT NULL DEFAULT (0),
    "copyRight" INTEGER NOT NULL DEFAULT (0),
    "mbId" TEXT
);
CREATE TABLE "tag" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "value" TEXT NOT NULL
);
CREATE TABLE "tagfile" (
    "idFile" INTEGER NOT NULL,
    "idTag" INTEGER NOT NULL,
	PRIMARY KEY ("idFile", "idTag"),
	FOREIGN KEY(idFile) REFERENCES file(idFile),
	FOREIGN KEY(idTag) REFERENCES tag(id) ON DELETE CASCADE
);
CREATE TABLE "playlist" (
    "idPlaylist" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT NOT NULL,
    "limitDo" INTEGER NOT NULL,
    "limitValue" INTEGER NOT NULL,
    "limitUnit" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "match" TEXT NOT NULL,
    "random" INTEGER NOT NULL,
    "hidden" INTEGER NOT NULL
);
CREATE TABLE "file" (
    "idFile" INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,
    "idPath" INTEGER NOT NULL,
    "name" TEXT NOT NULL,
    "rating" INTEGER NOT NULL,
    "lastPlayed" TEXT NOT NULL,
    "playCounter" INTEGER NOT NULL,
    "addedDate" TEXT NOT NULL,
    "artist" TEXT NOT NULL,
    "album" TEXT NOT NULL,
    "albumArtist" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "trackNo" INTEGER NOT NULL,
    "trackTotal" INTEGER NOT NULL,
    "discNo" INTEGER NOT NULL,
    "discTotal" INTEGER NOT NULL,
    "genre" TEXT NOT NULL,
    "year" TEXT NOT NULL,
    "BPM" REAL NOT NULL,
    "comment" TEXT NOT NULL,
    "nbCovers" INTEGER NOT NULL,
    "bitRate" TEXT NOT NULL,
    "format" TEXT NOT NULL,
    "length" INTEGER NOT NULL,
    "size" INTEGER NOT NULL,
    "modifDate" TEXT NOT NULL,
    "deleted" INTEGER NOT NULL,
    "coverHash" TEXT NOT NULL,
    "ratingModifDate" TEXT NOT NULL DEFAULT "1970-01-01 00:00:00",
    "tagsModifDate" TEXT NOT NULL  DEFAULT "1970-01-01 00:00:00",
    "genreModifDate" TEXT NOT NULL  DEFAULT "1970-01-01 00:00:00",
    "saved" INTEGER NOT NULL DEFAULT (0),
	FOREIGN KEY(idPath) REFERENCES path(idPath)
);
CREATE TABLE "client" (
    "idClient" INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,
    "login" TEXT NOT NULL,
     "pwd" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "idDevice" INTEGER,
    "idStatSource" INTEGER,
    "enabled" BOOL,
    CONSTRAINT name_unique UNIQUE ('login'),
    FOREIGN KEY(idStatSource) REFERENCES statSource(idStatSource) ON DELETE CASCADE,
    FOREIGN KEY(idDevice) REFERENCES device(idDevice) ON DELETE CASCADE
);
CREATE TABLE machine (
    "idMachine" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT NOT NULL,
    "description" TEXT,
    "hidden" INTEGER NOT NULL
);