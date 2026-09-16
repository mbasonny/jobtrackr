CREATE TABLE users (
    id        UUID         PRIMARY KEY,
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role      VARCHAR(20)  NOT NULL
);

CREATE UNIQUE INDEX idx_users_email ON users (email);
