create table sessions
(
    id          SERIAL    PRIMARY KEY,
    topic_id    INTEGER   NOT NULL,
    startzeit   TIMESTAMP NOT NULL,
    endzeit     TIMESTAMP NOT NULL,
    user_id     BIGINT    NOT NULL,

    CONSTRAINT fk_session_topic
        FOREIGN KEY (topic_id)
        REFERENCES topic(id),

    CONSTRAINT fk_session_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);