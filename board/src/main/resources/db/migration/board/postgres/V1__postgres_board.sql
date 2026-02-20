CREATE TABLE post (
    post_id VARCHAR(26) NOT NULL,
    user_id VARCHAR(26) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (post_id)
);

CREATE INDEX idx_post_active_id
    ON post (deleted_at, post_id);

CREATE TABLE comment (
    comment_id VARCHAR(26) NOT NULL,
    post_id VARCHAR(26) NOT NULL,
    user_id VARCHAR(26) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (comment_id)
--     CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post(post_id)
);

CREATE INDEX idx_comment_post_active_id
    ON comment (post_id, deleted_at, comment_id);

CREATE TABLE files (
    file_id        VARCHAR(26)  NOT NULL,
    user_id        VARCHAR(26),
    related_id     VARCHAR(26),
    related_type   VARCHAR(30),
    storage_key    VARCHAR(512) NOT NULL,
    storage        VARCHAR(20)  NOT NULL DEFAULT 'S3',
    file_name      VARCHAR(255),
    mime_type      VARCHAR(150),
    file_size      BIGINT,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMPTZ NULL,
    updated_at     TIMESTAMPTZ NULL,
    deleted_at     TIMESTAMPTZ NULL,
    PRIMARY KEY (file_id)
);

CREATE INDEX idx_files_related_id
    ON files (related_id);
