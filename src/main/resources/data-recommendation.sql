-- Вставка тестовых пользователей
MERGE INTO users (id, username, first_name, last_name, email) VALUES
('123e4567-e89b-12d3-a456-426614174000', 'sheron.berge', 'Sheron', 'Berge', 'sheron.berge@example.com');

MERGE INTO users (id, username, first_name, last_name, email) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'test.user', 'Test', 'User', 'test.user@example.com');

MERGE INTO users (id, username, first_name, last_name, email) VALUES
('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'john.doe', 'John', 'Doe', 'john.doe@example.com');

-- Вставка продуктов
MERGE INTO product (id, name, type, description) VALUES
('debit-001', 'Дебетовая карта', 'DEBIT', 'Основная дебетовая карта');

MERGE INTO product (id, name, type, description) VALUES
('credit-001', 'Кредитная карта', 'CREDIT', 'Кредитная карта с льготным периодом');

MERGE INTO product (id, name, type, description) VALUES
('saving-001', 'Накопительный счет', 'SAVING', 'Счет для накоплений');

MERGE INTO product (id, name, type, description) VALUES
('invest-001', 'Инвестиционный счет', 'INVEST', 'Счет для инвестиций');

-- Вставка тестовых транзакций
MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t1', '123e4567-e89b-12d3-a456-426614174000', 'debit-001', 1500000, 'DEPOSIT', DATEADD('DAY', -10, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t2', '123e4567-e89b-12d3-a456-426614174000', 'debit-001', 500000, 'WITHDRAW', DATEADD('DAY', -5, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t3', '123e4567-e89b-12d3-a456-426614174000', 'saving-001', 750000, 'DEPOSIT', DATEADD('DAY', -3, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t4', '550e8400-e29b-41d4-a716-446655440000', 'debit-001', 2000000, 'DEPOSIT', DATEADD('DAY', -15, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t5', '550e8400-e29b-41d4-a716-446655440000', 'debit-001', 1500000, 'WITHDRAW', DATEADD('DAY', -8, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t6', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'debit-001', 10000000, 'DEPOSIT', DATEADD('DAY', -20, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t7', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'credit-001', 5000000, 'WITHDRAW', DATEADD('DAY', -10, CURRENT_TIMESTAMP));

MERGE INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
('t8', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'invest-001', 3000000, 'DEPOSIT', DATEADD('DAY', -5, CURRENT_TIMESTAMP));