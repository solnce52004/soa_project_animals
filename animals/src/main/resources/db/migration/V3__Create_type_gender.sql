BEGIN TRANSACTION;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM "pg_type" WHERE "typname" = 'genders') THEN
            CREATE TYPE GENDER AS ENUM ('m', 'f', 'u');
        END IF;
    END
$$;

COMMIT;
