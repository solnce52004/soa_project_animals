BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "access_tokens"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "access_tokens_pk"
            PRIMARY KEY,
    "token" VARCHAR(255) NOT NULL
        CONSTRAINT "access_tokens_token_uindex"
            UNIQUE,
    "expires_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "user_id" BIGSERIAL NOT NULL
        CONSTRAINT "user_id_fk"
            REFERENCES "users"
);

CREATE SEQUENCE IF NOT EXISTS "access_tokens_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "access_tokens_id_seq" OWNED BY "access_tokens"."id";
ALTER TABLE IF EXISTS ONLY "access_tokens"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('access_tokens_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "access_tokens_token_index"
    ON "access_tokens" ("token");
CREATE INDEX IF NOT EXISTS "access_tokens_user_id_index"
    ON "access_tokens" ("user_id");

ALTER TABLE "access_tokens"
    OWNER TO "animal";

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "access_tokens" IS 'Access tokens';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'access_tokens'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
