INSERT INTO bank_account (id, account_number, account_holder_name, bank_name, created_at) VALUES
    ('fdf9c6a5-334d-4910-b48f-0247c8062967', '1234567890', 'John Doe', 'Bank A', NOW()),
    (uuid_generate_v4(), '0987654321', 'Jane Smith', 'Bank B', NOW());