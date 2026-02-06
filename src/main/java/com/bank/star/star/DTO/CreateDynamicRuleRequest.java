package com.bank.star.star.DTO;

import com.bank.star.star.entity.QueryType;

import java.util.List;

public class CreateDynamicRuleRequest {
    private String productName;
    private String productId;
    private String productText;
    private Boolean isActive;
    private List<ConditionDTO> conditions;

    // Геттеры и сеттеры
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<ConditionDTO> getConditions() { return conditions; }
    public void setConditions(List<ConditionDTO> conditions) { this.conditions = conditions; }

    public static class ConditionDTO {
        private QueryType query;
        private List<String> arguments;
        private Boolean negate;

        public QueryType getQuery() { return query; }
        public void setQuery(QueryType query) { this.query = query; }

        public List<String> getArguments() { return arguments; }
        public void setArguments(List<String> arguments) { this.arguments = arguments; }

        public Boolean getNegate() { return negate; }
        public void setNegate(Boolean negate) { this.negate = negate; }
    }
}