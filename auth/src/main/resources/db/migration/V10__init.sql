BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "auth_db"."public"."users"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "users_pk"
            PRIMARY KEY,
    "username" VARCHAR(255) DEFAULT 'GUEST'::VARCHAR NOT NULL,
    "password" VARCHAR(255),
    "status" VARCHAR(255) DEFAULT 'ACTIVE'::VARCHAR NOT NULL,
    "token" VARCHAR(255),
    "reset_token" VARCHAR(255),
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "auth_db"."public"."users_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "auth_db"."public"."users_id_seq" OWNED BY "auth_db"."public"."users"."id";
ALTER TABLE IF EXISTS ONLY "auth_db"."public"."users"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('auth_db.public.users_id_seq'::REGCLASS);

CREATE UNIQUE INDEX IF NOT EXISTS "users_username_uindex"
    ON "auth_db"."public"."users" ("username");
CREATE UNIQUE INDEX IF NOT EXISTS "users_token_uindex"
    ON "auth_db"."public"."users" ("token");


CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "auth_db"."public"."users" IS 'Пользователи';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'auth_db.public.users'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;

COMMIT;
