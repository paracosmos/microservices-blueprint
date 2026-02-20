CREATE TABLE users (
    user_id VARCHAR(26) NOT NULL,
    email VARCHAR(512) NOT NULL UNIQUE,
    name VARCHAR(512),              -- ENC
    password VARCHAR(512),          -- HASH
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (user_id)
);

CREATE INDEX idx_users_active_email
  ON users (email, deleted_at);

CREATE INDEX idx_users_active_user_id
  ON users (user_id, deleted_at);

CREATE TABLE provider (
    provider_id VARCHAR(26) NOT NULL,
    user_id VARCHAR(26) NOT NULL,
    provider_key VARCHAR(1) NOT NULL, -- 'GOOGLE:G', 'APPLE:A'
    provider_uid VARCHAR(200) NOT NULL,
    provider_name VARCHAR(512),
    provider_picture TEXT,
    created_at TIMESTAMP NULL,
    PRIMARY KEY (provider_id),
    UNIQUE (user_id, provider_key),
    CONSTRAINT fk_provider_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE INDEX idx_provider_user_id_provider_key
  ON provider (user_id, provider_key);
