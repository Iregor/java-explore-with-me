CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email varchar(255) NOT NULL,
name varchar(255) NOT NULL,
UNIQUE (email, name)
);

CREATE TABLE IF NOT EXISTS categories (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
annotation varchar(2000) NOT NULL,
category_id BIGINT REFERENCES categories(id) ON DELETE RESTRICT,
created_on TIMESTAMP NOT NULL,
description varchar(7000) NOT NULL,
event_date TIMESTAMP NOT NULL,
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
initiator_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
lat float NOT NULL,
lon float NOT NULL,
paid boolean NOT NULL,
participant_limit int NOT NULL,
published_on TIMESTAMP,
request_moderation boolean NOT NULL,
state varchar(32),
title varchar(120)
);

CREATE TABLE IF NOT EXISTS requests(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
requestor_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
status varchar(64) NOT NULL,
created TIMESTAMP NOT NULL,
UNIQUE(requestor_id, event_id)
);

CREATE TABLE IF NOT EXISTS compilations(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
title varchar(128) NOT NULL,
pinned boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_event(
compilation_id BIGINT NOT NULL,
event_id BIGINT NOT NULL,
PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comments(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
text varchar(300) NOT NULL,
created TIMESTAMP NOT NULL,
commentator_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
UNIQUE(commentator_id, event_id)
);