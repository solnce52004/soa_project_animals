BEGIN TRANSACTION;

INSERT INTO "animal_types" ("title")
VALUES ('cat'),
       ('dog'),
       ('caw'),
       ('bear'),
       ('elephant');

COMMIT;