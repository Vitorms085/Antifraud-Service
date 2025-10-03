-- Create required extensions

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create audit_data table

CREATE TABLE audit_data (
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create bank_account table

CREATE TABLE bank_account (
   id UUID PRIMARY KEY,
   account_number VARCHAR(255) NOT NULL,
   account_holder_name VARCHAR(255) NOT NULL,
   bank_name VARCHAR(255) NOT NULL
) INHERITS (audit_data);

-- Create bank_transaction table

CREATE TABLE bank_transaction (
   id UUID PRIMARY KEY,
   bank_account_id UUID NOT NULL REFERENCES bank_account(id),
   transaction_value NUMERIC(19,2) NOT NULL,
   type VARCHAR(255) NOT NULL,
   date_time TIMESTAMP NOT NULL
) INHERITS (audit_data);