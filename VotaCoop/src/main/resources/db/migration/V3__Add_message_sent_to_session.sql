-- V3__Add_message_sent_to_session.sql


-- Add message_sent column to session
ALTER TABLE session ADD COLUMN message_sent BOOLEAN DEFAULT FALSE NOT NULL;
