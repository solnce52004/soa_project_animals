BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "sign_in_logs"
(
    "id" BIGSERIAL NOT NULL
        CONSTRAINT "sign_in_logs_pk"
            PRIMARY KEY,
    "ip" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "sign_in_logs_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE IF EXISTS "sign_in_logs_id_seq" OWNED BY "sign_in_logs"."id";
ALTER TABLE IF EXISTS ONLY "sign_in_logs"
    ALTER COLUMN "id" SET DEFAULT NEXTVAL('sign_in_logs_id_seq'::REGCLASS);

CREATE INDEX IF NOT EXISTS "sign_in_logs_ip_index"
    ON "sign_in_logs" ("ip");

ALTER TABLE "sign_in_logs"
    OWNER TO "animal";

---
CREATE OR REPLACE FUNCTION "add_comments"()
    RETURNS VOID AS
$$
BEGIN
    COMMENT ON TABLE "sign_in_logs" IS 'Sign_in logs';
END
$$ LANGUAGE "plpgsql";

DO
$$
    BEGIN
        IF EXISTS(SELECT 'sign_in_logs'::REGCLASS) THEN
            PERFORM "add_comments"();
        END IF;
        DROP FUNCTION IF EXISTS "add_comments"();
    END
$$;
COMMIT;
