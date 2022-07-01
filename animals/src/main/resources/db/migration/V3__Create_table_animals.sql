---
BEGIN TRANSACTION;

DROP TYPE IF EXISTS genders;
CREATE TYPE genders AS ENUM ('m', 'f', 'n');

CREATE TABLE IF NOT EXISTS "public"."animals"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "animals_pk"
            PRIMARY KEY,
    "user_id" BIGINT NOT NULL
        CONSTRAINT "user_id_fk"
            REFERENCES "public"."users" ("id"),
    "animal_type_id" BIGINT NOT NULL
        CONSTRAINT "animal_type_id_fk"
            REFERENCES "public"."animal_types",
    "name" CHARACTER VARYING(255) NOT NULL,
    "gender" genders NOT NULL,
    "date_birthday" TIMESTAMP WITHOUT TIME ZONE DEFAULT '2000-01-01 00:00:00'::TIMESTAMP NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "public"."animals_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "public"."animals_id_seq" OWNED BY "public"."animals"."id";
ALTER TABLE IF EXISTS ONLY "public"."animals"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('public.animals_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "animals_name_index"
    ON "public"."animals" ("name");

---
CREATE OR REPLACE FUNCTION add_comments()
    RETURNS void AS $$
BEGIN
    COMMENT ON TABLE "public"."animals" IS 'Список всех животных всех пользователей';
    COMMENT ON COLUMN "public"."animals"."gender" is 'm - male, f - female, u - untitled';
END
$$ LANGUAGE plpgsql;

DO $$
    BEGIN
        IF EXISTS (SELECT 'public.animals'::regclass) THEN
            PERFORM add_comments();
        END IF;
        DROP FUNCTION IF EXISTS add_comments();
    END
$$;
COMMIT;
--