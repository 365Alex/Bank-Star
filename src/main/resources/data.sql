-- Создание таблицы users (в recommendations базе)
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы product
CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы transaction
CREATE TABLE IF NOT EXISTS transaction (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    amount BIGINT NOT NULL, -- сумма в копейках
    type VARCHAR(20) NOT NULL, -- DEPOSIT, WITHDRAW
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Вставка тестовых пользователей
INSERT INTO users (id, username, first_name, last_name, email) VALUES
('123e4567-e89b-12d3-a456-426614174000', 'sheron.berge', 'Sheron', 'Berge', 'sheron.berge@example.com'),
('550e8400-e29b-41d4-a716-446655440000', 'test.user', 'Test', 'User', 'test.user@example.com'),
('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'john.doe', 'John', 'Doe', 'john.doe@example.com');

-- Вставка продуктов
INSERT INTO product (id, name, type, description) VALUES
('debit-001', 'Дебетовая карта', 'DEBIT', 'Основная дебетовая карта'),
('credit-001', 'Кредитная карта', 'CREDIT', 'Кредитная карта с льготным периодом'),
('saving-001', 'Накопительный счет', 'SAVING', 'Счет для накоплений'),
('invest-001', 'Инвестиционный счет', 'INVEST', 'Счет для инвестиций');

-- Вставка тестовых транзакций
INSERT INTO transaction (id, user_id, product_id, amount, type, created_at) VALUES
-- Для пользователя sheron.berge
(gen_random_uuid(), '123e4567-e89b-12d3-a456-426614174000', 'debit-001', 1500000, 'DEPOSIT', DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(gen_random_uuid(), '123e4567-e89b-12d3-a456-426614174000', 'debit-001', 500000, 'WITHDRAW', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(gen_random_uuid(), '123e4567-e89b-12d3-a456-426614174000', 'saving-001', 750000, 'DEPOSIT', DATEADD('DAY', -3, CURRENT_TIMESTAMP)),

-- Для пользователя test.user
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440000', 'debit-001', 2000000, 'DEPOSIT', DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440000', 'debit-001', 1500000, 'WITHDRAW', DATEADD('DAY', -8, CURRENT_TIMESTAMP)),

-- Для пользователя john.doe
(gen_random_uuid(), '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'debit-001', 10000000, 'DEPOSIT', DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
(gen_random_uuid(), '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'credit-001', 5000000, 'WITHDRAW', DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(gen_random_uuid(), '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'invest-001', 3000000, 'DEPOSIT', DATEADD('DAY', -5, CURRENT_TIMESTAMP));

-- Создание индексов для производительности
CREATE INDEX IF NOT EXISTS idx_transaction_user_id ON transaction(user_id);
CREATE INDEX IF NOT EXISTS idx_transaction_product_id ON transaction(product_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);