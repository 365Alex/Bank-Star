package com.bank.star.star.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RULE_STATISTICS")
public class RuleStatistic {

    @Id
    private String id;

    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "execution_count", nullable = false)
    private Long executionCount = 0L;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public RuleStatistic() {}

    public RuleStatistic(String ruleId, String ruleName) {
        this.id = ruleId;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.executionCount = 0L;
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementCount() {
        this.executionCount++;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public Long getExecutionCount() { return executionCount; }
    public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}