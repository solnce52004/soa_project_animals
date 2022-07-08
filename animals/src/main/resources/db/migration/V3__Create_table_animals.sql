BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS "animals_db"."public"."animals"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "animals_pk"
            PRIMARY KEY,
    "username" VARCHAR(255) NOT NULL,
    "animal_type_id" BIGSERIAL NOT NULL
        CONSTRAINT "animal_type_id_fk"
            REFERENCES "animal_types",
    "animal_name" CHARACTER VARYING(255) NOT NULL
        CONSTRAINT "animalname_uindex"
            UNIQUE,
    "gender" GENDER NOT NULL DEFAULT 'u'::GENDER,
    "birthdate" TIMESTAMP DEFAULT '2000-01-01 00:00:00+3'::TIMESTAMP NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "animals_db"."public"."animals_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "animals_db"."public"."animals_id_seq" OWNED BY "animals_db"."public"."animals"."id";
ALTER TABLE IF EXISTS ONLY "animals_db"."public"."animals"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('animals_db.public.animals_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "animals_name_index"
    ON "animals_db"."public"."animals" ("animal_name");

CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "animals_db"."public"."animals" IS 'List of all animals of all users';
    COMMENT ON COLUMN "animals_db"."public"."animals"."gender" IS 'm - male, f - female, u - untitled';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'animals_db.public.animals'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
