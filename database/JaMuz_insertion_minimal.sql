BEGIN TRANSACTION;

INSERT INTO "versionHistory" ("version", "upgradeStart", "upgradeEnd") 
	VALUES ('2', datetime('now'), datetime('now'));

INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('1', 'location.library', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('2', 'library.isMaster', 'false', 'bool');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('3', 'location.add', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('4', 'location.ok', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('5', 'location.ko', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('6', 'network.proxy', '', 'proxy');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('7', 'location.mask', '%albumartist%/%album%/%track% %title%', 'mask');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('8', 'log.level', 'INFO', 'list');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('9', 'log.limit', '5242880', 'integer');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('10', 'log.count', '20', 'integer');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('11', 'files.audio', 'mp3,flac', 'csv');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('12', 'files.image', 'png,jpg,jpeg,bmp,gif', 'csv');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('13', 'files.convert', '', 'csv');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('14', 'files.delete', '', 'csv');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('15', 'location.manual', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('16', 'location.transcoded', '', 'path');
INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('17', 'files.image.delete', 'false', 'bool');
COMMIT;
