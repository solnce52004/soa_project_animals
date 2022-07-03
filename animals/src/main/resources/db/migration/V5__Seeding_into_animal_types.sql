BEGIN TRANSACTION;

INSERT INTO "animals_db"."public"."animal_types" ("title")
VALUES ('cat'),
       ('dog'),
       ('caw'),
       ('bear'),
       ('elephant');

COMMIT;