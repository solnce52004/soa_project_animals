BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "roles"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "roles_pk"
            PRIMARY KEY,
    "title" VARCHAR(255) DEFAULT 'ROLE_USER'::VARCHAR NOT NULL
        CONSTRAINT "roles_title_uindex" UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS "roles_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "roles_id_seq" OWNED BY "roles"."id";
ALTER TABLE IF EXISTS ONLY "roles"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('roles_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "roles_title_index"
    ON "roles" ("title");
---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "roles" IS 'Roles';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'roles'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;

COMMIT;