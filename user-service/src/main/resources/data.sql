DELETE FROM user;
INSERT INTO user VALUES (0, unix_timestamp(now()), unix_timestamp(now()), 'john.doe@example.com', 'John', 'Doe', 'user');