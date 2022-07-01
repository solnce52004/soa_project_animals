
---
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "public"."animal_types"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "animal_types_pk"
            PRIMARY KEY,
    "title" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "public"."animal_types_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "public"."animal_types_id_seq" OWNED BY "public"."animal_types"."id";
ALTER TABLE IF EXISTS ONLY "public"."animal_types"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('public.animal_types_id_seq'::REGCLASS);

CREATE UNIQUE INDEX IF NOT EXISTS "animal_types_title_uindex"
    ON "animal_types" ("title");


---
CREATE OR REPLACE FUNCTION add_comments()
    RETURNS void AS $$
BEGIN
    COMMENT ON TABLE "animal_types" IS 'Список-справочник видов животных';
END
$$ LANGUAGE plpgsql;

DO $$
    BEGIN
        IF EXISTS (SELECT 'animal_types'::regclass) THEN
            PERFORM add_comments();
        END IF;
        DROP FUNCTION IF EXISTS add_comments();
    END
$$;
COMMIT;
--