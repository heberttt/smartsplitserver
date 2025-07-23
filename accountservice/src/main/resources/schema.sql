-- DROP TABLE accounts;
-- CREATE TABLE IF NOT EXISTS accounts (
--     id VARCHAR(50),
--     username VARCHAR(50) NOT NULL,
--     email VARCHAR(254) NOT NULL UNIQUE,
--     profilePictureLink VARCHAR(254) NOT NULL,
--     PRIMARY KEY (id)
-- );

-- CREATE TABLE friendships (
--   account1_id VARCHAR(50) REFERENCES accounts(id),
--   account2_id VARCHAR(50) REFERENCES accounts(id),
--   created_at TIMESTAMP DEFAULT now(),
--   PRIMARY KEY (account1_id, account2_id),
--   CHECK (account1_id < account2_id)
-- );

-- CREATE TABLE friendship_request (
--     id SERIAL PRIMARY KEY,
--     initiator_id VARCHAR(50) REFERENCES accounts(id),
--     target_id VARCHAR(50) REFERENCES accounts(id),
--     status VARCHAR(50) NOT NULL,
--     created_at TIMESTAMP DEFAULT now()
-- );

-- CREATE TABLE groups (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(50) NOT NULL,
--     description TEXT,
--     created_at TIMESTAMP DEFAULT now()
-- );

-- CREATE TABLE group_members (
--     group_id INTEGER REFERENCES groups(id) ON DELETE CASCADE,
--     account_id VARCHAR(50) REFERENCES accounts(id) ON DELETE CASCADE,
--     joined_at TIMESTAMP DEFAULT now(),
--     role VARCHAR(50) DEFAULT 'MEMBER',
--     PRIMARY KEY (group_id, account_id)
-- );
