INSERT INTO "optiontype" ("idOptionType", "name", "default", "type")
VALUES (18, 'audio.main.output', '', 'string');

INSERT INTO "optiontype" ("idOptionType", "name", "default", "type")
VALUES (19, 'audio.preview.output', '', 'string');

-- Insert default values for existing machines
INSERT INTO option ('idMachine', 'idOptionType', 'value')
SELECT idMachine, 18, ''
FROM machine;

INSERT INTO option ('idMachine', 'idOptionType', 'value')
SELECT idMachine, 19, ''
FROM machine;

