BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "refresh_tokens"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "refresh_tokens_pk"
            PRIMARY KEY,
    "token" VARCHAR(255) NOT NULL
        CONSTRAINT "refresh_tokens_token_uindex"
            UNIQUE,
    "expires_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "user_id" BIGSERIAL NOT NULL
        CONSTRAINT "user_id_fk"
            REFERENCES "users"
);

CREATE SEQUENCE IF NOT EXISTS "refresh_tokens_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "refresh_tokens_id_seq" OWNED BY "refresh_tokens"."id";
ALTER TABLE IF EXISTS ONLY "refresh_tokens"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('refresh_tokens_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "refresh_tokens_token_index"
    ON "refresh_tokens" ("token");
CREATE INDEX IF NOT EXISTS "refresh_tokens_user_id_index"
    ON "refresh_tokens" ("user_id");

ALTER TABLE "refresh_tokens"
    OWNER TO "animal";

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "refresh_tokens" IS 'Refresh tokens';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'refresh_tokens'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
