CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE link (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alias           VARCHAR(16) NOT NULL UNIQUE,
    original_url    TEXT NOT NULL,
    created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255)
);

CREATE INDEX idx_alias ON link(alias);