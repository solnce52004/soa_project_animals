BEGIN TRANSACTION;

INSERT INTO "permissions" ("title")
VALUES ('READ'),
       ('WRITE');

COMMIT;