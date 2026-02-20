CREATE TABLE terms (
  terms_id     VARCHAR(26) PRIMARY KEY,
  type         VARCHAR(50) NOT NULL,  -- TOS, PRIVACY
  title        VARCHAR(200) NOT NULL,
  content      TEXT NOT NULL,
  content_url  VARCHAR(500) NULL,
  required     BOOLEAN NOT NULL DEFAULT FALSE,
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_terms_type_created
  ON terms(type, created_at DESC);

CREATE TABLE user_terms_agreement (
  user_id     VARCHAR(26) NOT NULL,
  terms_id    VARCHAR(26) NOT NULL,
  agree       BOOLEAN NOT NULL,
  created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, terms_id)
);

CREATE INDEX idx_uta_terms
  ON user_terms_agreement(terms_id);
