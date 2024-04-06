DROP TABLE IF EXISTS compilation_events_id;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS category;
DROP TYPE IF EXISTS STATE;
DROP TABLE IF EXISTS users;

CREATE TYPE STATE AS ENUM ('PENDING', 'PUBLISHED', 'CANCELED');

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category(
   id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS events(
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000),
    category INT REFERENCES category(id),
    confirmed_requests INT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    description VARCHAR(7000),
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator INT REFERENCES users(id),
    lat FLOAT,
    lon FLOAT,
    paid BOOLEAN,
    participant_limit INT,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR(40),
    title VARCHAR(120),
    views INT
);

CREATE TABLE IF NOT EXISTS request(
   id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   created TIMESTAMP,
   event BIGINT REFERENCES events(id),
   requester BIGINT,
   status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS compilations(
   id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   events BIGINT,
   pinned BOOLEAN,
   title VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS compilation_events_id(
    compilations_id BIGINT,
    event_id BIGINT,
    pinned BOOLEAN,
    title VARCHAR
);