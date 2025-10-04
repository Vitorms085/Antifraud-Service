-- Create suspicious_bank_transaction table

CREATE TABLE suspicious_bank_transaction (
   id UUID PRIMARY KEY,
   bank_transaction_id UUID NOT NULL REFERENCES bank_transaction(id),
   reason VARCHAR(255) NOT NULL,
   status VARCHAR(255) NOT NULL
) INHERITS (audit_data);