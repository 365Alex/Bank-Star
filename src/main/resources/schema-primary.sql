-- Таблица для динамических правил
CREATE TABLE IF NOT EXISTS dynamic_rules (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL UNIQUE,
    product_text TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    conditions_json TEXT
);

-- Таблица для условий правил
CREATE TABLE IF NOT EXISTS rule_conditions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_id UUID,
    query VARCHAR(50) NOT NULL,
    negate BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (rule_id) REFERENCES dynamic_rules(id) ON DELETE CASCADE
);

-- Таблица для аргументов условий
CREATE TABLE IF NOT EXISTS rule_condition_arguments (
    condition_id BIGINT,
    argument VARCHAR(255) NOT NULL,
    FOREIGN KEY (condition_id) REFERENCES rule_conditions(id) ON DELETE CASCADE
);

-- Таблица для статистики правил
CREATE TABLE IF NOT EXISTS rule_statistics (
    id VARCHAR(255) PRIMARY KEY,
    rule_id VARCHAR(255) NOT NULL,
    rule_name VARCHAR(255),
    execution_count BIGINT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для динамических рекомендаций
CREATE TABLE IF NOT EXISTS dynamic_recommendations (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);