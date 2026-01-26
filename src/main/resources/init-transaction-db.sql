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

-- Вставляем тестовые транзакции для пользователя f37ba8a8-3cd5-4976-9f74-2b21f105da67
INSERT INTO transaction (id, user_id, product_id, type, amount) VALUES
-- Дебетовые транзакции (больше пополнений, чем снятий)
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '11111111-1111-1111-1111-111111111111', 'DEPOSIT', 200000), -- 2000 руб
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '11111111-1111-1111-1111-111111111111', 'WITHDRAW', 150000), -- 1500 руб
-- Сберегательные транзакции (больше 1000 руб)
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '22222222-2222-2222-2222-222222222222', 'DEPOSIT', 150000); -- 1500 руб
-- Большие пополнения DEBIT (>500000 руб)
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '11111111-1111-1111-1111-111111111111', 'DEPOSIT', 6000000), -- 60000 руб
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '11111111-1111-1111-1111-111111111111', 'DEPOSIT', 8000000), -- 80000 руб

-- Большие пополнения SAVING (>200000 руб)
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '22222222-2222-2222-2222-222222222222', 'DEPOSIT', 3000000), -- 30000 руб
('gggggggg-gggg-gggg-gggg-gggggggggggg', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '22222222-2222-2222-2222-222222222222', 'DEPOSIT', 4000000), -- 40000 руб

-- Небольшие снятия (чтобы пополнения > снятий)
('hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh', 'f37ba8a8-3cd5-4976-9f74-2b21f105da67', '11111111-1111-1111-1111-111111111111', 'WITHDRAW', 1000000); -- 10000 руб

-- Для e61cc8e6-f3e2-4d8c-9a58-a379f9205a4e:
INSERT INTO transaction (id, user_id, product_id, type, amount) VALUES
-- Петров имеет кредитные продукты (не должен получать premium credit card)
('iiiiiiii-iiii-iiii-iiii-iiiiiiiiiiii', 'e61cc8e6-f3e2-4d8c-9a58-a379f9205a4e', '44444444-4444-4444-4444-444444444444', 'WITHDRAW', 2000000), -- 20000 руб

-- Маленькие пополнения DEBIT
('jjjjjjjj-jjjj-jjjj-jjjj-jjjjjjjjjjjj', 'e61cc8e6-f3e2-4d8c-9a58-a379f9205a4e', '11111111-1111-1111-1111-111111111111', 'DEPOSIT', 100000), -- 1000 руб

-- Маленькие пополнения SAVING
('kkkkkkkk-kkkk-kkkk-kkkk-kkkkkkkkkkkk', 'e61cc8e6-f3e2-4d8c-9a58-a379f9205a4e', '22222222-2222-2222-2222-222222222222', 'DEPOSIT', 50000); -- 500 руб


INSERT INTO dynamic_rules (id, product_name, product_id, product_text, is_active)
VALUES (
    'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee',
    'Премиальная кредитная карта',
    'premium-credit-card-001',
    'Премиальная кредитная карта с повышенным кэшбэком и бесплатным обслуживанием для наших лучших клиентов!',
    true
);

-- Добавление условий для правила
INSERT INTO rule_conditions (id, query, negate, rule_id) VALUES
(1, 'USER_OF', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee'),
(2, 'TRANSACTION_SUM_COMPARE', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee'),
(3, 'USER_OF', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee');

-- Аргументы для условий
INSERT INTO rule_condition_arguments (condition_id, argument) VALUES
(1, 'DEBIT'),  -- USER_OF DEBIT
(2, 'DEBIT'),  -- TRANSACTION_SUM_COMPARE DEBIT
(2, 'DEPOSIT'), -- DEPOSIT транзакции
(2, '500000'),  -- Сумма > 500000 руб (5000 в копейках)
(3, 'CREDIT');  -- NOT USER_OF CREDIT

