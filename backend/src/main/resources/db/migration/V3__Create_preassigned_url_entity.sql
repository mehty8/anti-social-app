CREATE TABLE preassigned_url_entity (
                                        id BIGSERIAL PRIMARY KEY,
                                        video_name VARCHAR(255),
                                        preassigned_Url TEXT,
                                        bucket_name VARCHAR(255),
                                        expiration_time TIMESTAMP,
                                        receiver_id BIGINT,
                                        sender_id BIGINT,
                                        FOREIGN KEY (receiver_id) REFERENCES user_entity(id),
                                        FOREIGN KEY (sender_id) REFERENCES user_entity(id)
);