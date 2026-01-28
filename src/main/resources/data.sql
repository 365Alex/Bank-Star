-- Вставляем статические правила как динамические (для примера)
INSERT INTO dynamic_rules (id, product_name, product_id, product_text, is_active, rule_conditions) VALUES
('147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Invest 500', '147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Откройте свой путь к успеху...', true,
'[
  {"query": "USER_OF", "arguments": ["DEBIT"], "negate": false},
  {"query": "USER_OF", "arguments": ["INVEST"], "negate": true},
  {"query": "TRANSACTION_SUM_COMPARE", "arguments": ["SAVING", "DEPOSIT", ">", "1000"], "negate": false}
]'),

('59efc529-2fff-41af-baff-90ccd7402925', 'Top Saving', '59efc529-2fff-41af-baff-90ccd7402925', 'Откройте свою собственную «Копилку»...', true,
'[
  {"query": "USER_OF", "arguments": ["DEBIT"], "negate": false},
  {"query": "TRANSACTION_SUM_COMPARE", "arguments": ["DEBIT", "DEPOSIT", ">=", "50000"], "negate": false}
]'),

('ab138afb-f3ba-4a93-b74f-0fcee86d447f', 'Простой кредит', 'ab138afb-f3ba-4a93-b74f-0fcee86d447f', 'Откройте мир выгодных кредитов...', true,
'[
  {"query": "USER_OF", "arguments": ["CREDIT"], "negate": true},
  {"query": "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", "arguments": ["DEBIT", ">"], "negate": false},
  {"query": "TRANSACTION_SUM_COMPARE", "arguments": ["DEBIT", "WITHDRAW", ">", "100000"], "negate": false}
]');

-- Статистика
INSERT INTO rule_statistics (id, rule_id, rule_name, execution_count, last_updated) VALUES
('stat-001', '147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Invest 500', 0, CURRENT_TIMESTAMP),
('stat-002', '59efc529-2fff-41af-baff-90ccd7402925', 'Top Saving', 0, CURRENT_TIMESTAMP),
('stat-003', 'ab138afb-f3ba-4a93-b74f-0fcee86d447f', 'Простой кредит', 0, CURRENT_TIMESTAMP);