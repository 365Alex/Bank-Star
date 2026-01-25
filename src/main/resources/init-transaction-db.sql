-- Создаем тестовую структуру для базы транзакций
CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Вставляем тестовые продукты
INSERT INTO product (id, type, name) VALUES
('11111111-1111-1111-1111-111111111111', 'DEBIT', 'Основной дебетовый счет'),
('22222222-2222-2222-2222-222222222222', 'SAVING', 'Накопительный счет'),
('33333333-3333-3333-3333-333333333333', 'INVEST', 'Инвестиционный счет'),
('44444444-4444-4444-4444-444444444444', 'CREDIT', 'Кредитный счет');

-- Вставляем тестовые транзакции для пользователя 123e4567-e89b-12d3-a456-426614174000 (ivanov)
INSERT INTO transaction (id, user_id, product_id, type, amount) VALUES
-- Дебетовые транзакции (больше пополнений, чем снятий)
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '123e4567-e89b-12d3-a456-426614174000', '11111111-1111-1111-1111-111111111111', 'DEPOSIT', 200000), -- 2000 руб
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '123e4567-e89b-12d3-a456-426614174000', '11111111-1111-1111-1111-111111111111', 'WITHDRAW', 150000), -- 1500 руб
-- Сберегательные транзакции (больше 1000 руб)
('cccccccc-cccc-cccc-cccc-cccccccccccc', '123e4567-e89b-12d3-a456-426614174000', '22222222-2222-2222-2222-222222222222', 'DEPOSIT', 150000); -- 1500 руб