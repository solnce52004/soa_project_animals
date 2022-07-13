BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "user_role"
(
    "user_id" BIGINT NOT NULL
        CONSTRAINT "user_id_fk"
            REFERENCES "users",
    "role_id" BIGINT NOT NULL
        CONSTRAINT "role_id_fk"
            REFERENCES "roles",
    CONSTRAINT "user_role_pkey"
        PRIMARY KEY ("user_id", "role_id")
);

ALTER TABLE "user_role"
    OWNER TO "animal";
COMMIT;