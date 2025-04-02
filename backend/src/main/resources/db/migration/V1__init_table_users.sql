CREATE TABLE users
(
    id        BIGSERIAL    NOT NULL,
    email     VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);