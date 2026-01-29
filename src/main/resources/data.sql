-- Тестовые данные для JPA базы (Primary база данных)

-- Вставка динамических правил
INSERT INTO dynamic_rules (id, product_name, product_id, product_text, is_active) VALUES
('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'Премиальная кредитная карта', 'premium-card-001', 'Премиальная кредитная карта с увеличенным лимитом', true),
('550e8400-e29b-41d4-a716-446655440000', 'Накопительный счет Премиум', 'saving-premium-001', 'Накопительный счет с повышенной процентной ставкой', true),
('147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Инвестиционный портфель', 'investment-portfolio-001', 'Готовый инвестиционный портфель от аналитиков', false);

-- Вставка статистики правил
INSERT INTO rule_statistics (id, rule_id, rule_name, execution_count, last_updated) VALUES
('stat-001', 'ab138afb-f3ba-4a93-b74f-0fcee86d447f', 'SimpleCreditRule', 45, CURRENT_TIMESTAMP),
('stat-002', '59efc529-2fff-41af-baff-90ccd7402925', 'TopSavingRule', 67, CURRENT_TIMESTAMP),
('stat-003', '147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Invest500Rule', 32, CURRENT_TIMESTAMP);

-- Вставка динамических рекомендаций
INSERT INTO dynamic_recommendations (id, name, text, created_at) VALUES
('rec-001', 'Сезонные предложения', 'Специальные предложения для клиентов', CURRENT_TIMESTAMP),
('rec-002', 'Новогодние акции', 'Акции и скидки от партнеров', CURRENT_TIMESTAMP);