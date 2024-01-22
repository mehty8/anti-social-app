CREATE TABLE role_entity (
                      id BIGSERIAL PRIMARY KEY,
                      role_name VARCHAR(255)
);

INSERT INTO role_entity (role_name) VALUES ('Admin'), ('User');