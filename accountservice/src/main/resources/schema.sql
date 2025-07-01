-- DROP TABLE accounts;
CREATE TABLE IF NOT EXISTS accounts (
    id VARCHAR(50),
    username VARCHAR(50) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    profilePictureLink VARCHAR(254) NOT NULL,
    PRIMARY KEY (id)
);