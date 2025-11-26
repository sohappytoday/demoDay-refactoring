-- user_provider: SMALLINT -> VARCHAR(255)
ALTER TABLE users
    MODIFY COLUMN user_provider VARCHAR(255);