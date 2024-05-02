CREATE TABLE user_entity_friends_requests_sent (
                                                   user_entity_id BIGINT,
                                                   friends_requests_sent VARCHAR(255),
                                                   FOREIGN KEY (user_entity_id) REFERENCES user_entity(id)
);