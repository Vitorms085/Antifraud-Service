INSERT INTO bank_account (id, credit_limit, debit_limit, is_active, created_at) VALUES
('fdf9c6a5-334d-4910-b48f-0247c8062967', 1000.00, 500.00, TRUE, NOW()),
(uuid_generate_v4(), 2000.00, 1000.00, TRUE, NOW()),
(uuid_generate_v4(), 1500.00, 750.00, FALSE, NOW());