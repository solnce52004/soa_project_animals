BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "public"."users"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "users_pk"
            PRIMARY KEY,
    "username" VARCHAR(255) DEFAULT 'GUEST'::CHARACTER NOT NULL,
    "password" VARCHAR(255),
    "status" VARCHAR(255) DEFAULT 'ACTIVE'::CHARACTER NOT NULL,
    "token" VARCHAR(255),
    "provider" VARCHAR(50),
    "reset_token" VARCHAR(255),
    "created_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "public"."users_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "public"."users_id_seq" OWNED BY "public"."users"."id";
ALTER TABLE IF EXISTS ONLY "public"."users"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('public.users_id_seq'::REGCLASS);

CREATE UNIQUE INDEX IF NOT EXISTS "users_username_uindex"
    ON "public"."users" ("username");
CREATE UNIQUE INDEX IF NOT EXISTS "users_token_uindex"
    ON "public"."users" ("token");

---
CREATE OR REPLACE FUNCTION add_comments()
    RETURNS void AS $$
BEGIN
    COMMENT ON TABLE "public"."users" IS 'Пользователи';
END
$$ LANGUAGE plpgsql;

DO $$
    BEGIN
        IF EXISTS (SELECT 'public.users'::regclass) THEN
            PERFORM add_comments();
        END IF;
        DROP FUNCTION IF EXISTS add_comments();
    END
$$;

COMMIT;