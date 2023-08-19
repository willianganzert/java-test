-- V1__Create_initial_tables.sql

-- Creating Topic table
CREATE TABLE topic (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT
);

-- Creating Session table
CREATE TABLE session (
                         id SERIAL PRIMARY KEY,
                         topic_id INT REFERENCES topic(id),
                         start_time TIMESTAMP NOT NULL,
                         end_time TIMESTAMP NOT NULL,
                         duration INT DEFAULT 60 -- Represents duration in seconds, with a default of 60 seconds (1 minute)
);

-- Creating Vote table
CREATE TABLE vote (
                      id SERIAL PRIMARY KEY,
                      session_id INT REFERENCES session(id),
                      member_cpf VARCHAR(11) UNIQUE NOT NULL,
                      vote_value BOOLEAN NOT NULL,
                      UNIQUE(session_id, member_cpf)
);