package com.bank.star.star.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RuleStatsResponse {

    @JsonProperty("stats")
    private List<RuleStat> stats;

    public RuleStatsResponse() {}

    public RuleStatsResponse(List<RuleStat> stats) {
        this.stats = stats;
    }

    public List<RuleStat> getStats() { return stats; }
    public void setStats(List<RuleStat> stats) { this.stats = stats; }

    public static class RuleStat {
        @JsonProperty("rule_id")
        private String ruleId;

        @JsonProperty("count")
        private Long count;

        public RuleStat() {}

        public RuleStat(String ruleId, Long count) {
            this.ruleId = ruleId;
            this.count = count;
        }

        public String getRuleId() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId = ruleId; }

        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}