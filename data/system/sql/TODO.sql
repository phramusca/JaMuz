INSERT INTO "optiontype" ("idOptionType", "name", "default", "type") VALUES ('18', 'location.slsk', './', 'path');
INSERT INTO option ('idMachine', 'idOptionType', 'value') SELECT idMachine, 18, '' FROM machine;