BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "animal_types"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "animal_types_pk"
            PRIMARY KEY,
    "title" VARCHAR(255) NOT NULL
        CONSTRAINT "animal_types_title_uindex"
            UNIQUE,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "animal_types_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "animal_types_id_seq" OWNED BY "animal_types"."id";
ALTER TABLE IF EXISTS ONLY "animal_types"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('animal_types_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "animal_types_title_index"
    ON "animal_types" ("title");

ALTER TABLE "animal_types"
    OWNER TO "animal";

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "animal_types" IS 'List-directory of animal species';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'animal_types'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
