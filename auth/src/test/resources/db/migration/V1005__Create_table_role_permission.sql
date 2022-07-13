BEGIN TRANSACTION;
CREATE TABLE "role_permission"
(
    "role_id" BIGINT NOT NULL
        CONSTRAINT "role_id_fk"
            REFERENCES "roles",
    "permission_id" BIGINT NOT NULL
        CONSTRAINT "permission_id_fk"
            REFERENCES "permissions",
    CONSTRAINT "role_permission_pkey"
        PRIMARY KEY ("role_id", "permission_id")
);

ALTER TABLE "role_permission"
    OWNER TO "animal";
COMMIT;