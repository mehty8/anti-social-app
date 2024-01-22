CREATE TABLE user_entity (
                             id BIGSERIAL PRIMARY KEY,
                             username VARCHAR(255),
                             password VARCHAR(255)
);

CREATE TABLE user_entity_roles (
                                   user_entity_id BIGINT,
                                   roles_id BIGINT,
                                   PRIMARY KEY (user_entity_id, roles_id),
                                   FOREIGN KEY (user_entity_id) REFERENCES user_entity(id),
                                   FOREIGN KEY (roles_id) REFERENCES role_entity(id)
);

CREATE TABLE user_entity_friends_names (
                                          user_entity_id BIGINT,
                                          friends_names VARCHAR(255),
                                          FOREIGN KEY (user_entity_id) REFERENCES user_entity(id)
);

CREATE TABLE user_entity_friends_requests (
                                             user_entity_id BIGINT,
                                             friends_requests VARCHAR(255),
                                             FOREIGN KEY (user_entity_id) REFERENCES user_entity(id)
);