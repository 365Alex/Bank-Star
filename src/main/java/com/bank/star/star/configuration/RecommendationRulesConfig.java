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
    }
}
