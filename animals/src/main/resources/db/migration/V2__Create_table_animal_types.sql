BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "animals_db"."public"."animal_types"
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

CREATE SEQUENCE IF NOT EXISTS "animals_db"."public"."animal_types_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "animals_db"."public"."animal_types_id_seq" OWNED BY "animals_db"."public"."animal_types"."id";
ALTER TABLE IF EXISTS ONLY "animals_db"."public"."animal_types"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('animals_db.public.animal_types_id_seq'::REGCLASS);

ALTER TABLE "animals_db"."public"."animal_types"
    OWNER TO "animal";

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "animals_db"."public"."animal_types" IS 'Список-справочник видов животных';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'animals_db.public.animal_types'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
