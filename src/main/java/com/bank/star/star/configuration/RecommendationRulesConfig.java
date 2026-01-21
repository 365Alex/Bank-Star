package com.bank.star.star.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ConfigurationProperties(prefix = "recommendation.rules")
public class RecommendationRulesConfig {
    private Map<String, RuleConfig> configs = new HashMap<>();

    public static class RuleConfig {
        private boolean enabled = true;
        private int priority = 0;
        private Map<String, Object> parameters = new HashMap<>();

        // Геттеры и сеттеры
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }

        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    // Геттеры и сеттеры для configs
    public Map<String, RuleConfig> getConfigs() { return configs; }
    public void setConfigs(Map<String, RuleConfig> configs) { this.configs = configs; }
}
