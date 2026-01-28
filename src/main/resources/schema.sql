-- Таблица для динамических правил
CREATE TABLE IF NOT EXISTS dynamic_rules (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL UNIQUE,
    product_text TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    rule_conditions TEXT
);

-- Таблица для статистики правил
CREATE TABLE IF NOT EXISTS rule_statistics (
    id VARCHAR(255) PRIMARY KEY,
    rule_id VARCHAR(255) NOT NULL,
    rule_name VARCHAR(255),
    execution_count BIGINT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);