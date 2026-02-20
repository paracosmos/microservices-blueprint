CREATE TABLE push_subscription (
  push_subscription_id VARCHAR(26) PRIMARY KEY,
  user_id VARCHAR(26) NOT NULL,
  endpoint VARCHAR(500) NOT NULL UNIQUE,
  p256dh VARCHAR(255) NOT NULL,
  auth VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL
);

CREATE INDEX idx_push_sub_user_active
    ON push_subscription(user_id, active);
