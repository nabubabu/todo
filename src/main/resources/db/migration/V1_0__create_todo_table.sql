CREATE TABLE todo (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    created TIMESTAMP(6) WITHOUT TIME ZONE,
    name CHARACTER VARYING(255) NOT NULL,
    deadline TIMESTAMP(6) WITHOUT TIME ZONE,
    priority CHARACTER VARYING(255)
);