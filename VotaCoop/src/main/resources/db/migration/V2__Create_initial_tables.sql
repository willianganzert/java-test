-- V2__Remove_unique_member_cpf_constraint.sql

-- Remove unique constraint on member_cpf
ALTER TABLE vote DROP CONSTRAINT vote_member_cpf_key;