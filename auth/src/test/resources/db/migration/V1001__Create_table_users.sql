BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "users"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "users_pk"
            PRIMARY KEY,
    "username" VARCHAR(255) DEFAULT 'GUEST'::VARCHAR NOT NULL
        CONSTRAINT "users_username_uindex" UNIQUE,
    "password" VARCHAR(255),
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "users_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "users_id_seq" OWNED BY "users"."id";
ALTER TABLE IF EXISTS ONLY "users"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('users_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "users_username_index"
    ON "users" ("username");

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "users" IS 'Users';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'users'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;

COMMIT;
