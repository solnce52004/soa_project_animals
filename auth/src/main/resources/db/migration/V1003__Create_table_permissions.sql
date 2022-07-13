BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "permissions"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "permissions_pk"
            PRIMARY KEY,
    "title" VARCHAR(255) DEFAULT 'WRITE'::VARCHAR NOT NULL
        CONSTRAINT "permissions_title_uindex" UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS "permissions_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "permissions_id_seq" OWNED BY "permissions"."id";
ALTER TABLE IF EXISTS ONLY "permissions"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('permissions_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "permissions_title_index"
    ON "permissions" ("title");
---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "permissions" IS 'Permissions';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'permissions'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;

COMMIT;