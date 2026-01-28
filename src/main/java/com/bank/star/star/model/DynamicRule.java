package com.bank.star.star.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_text", columnDefinition = "TEXT", nullable = false)
    private String productText;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "rule_conditions", columnDefinition = "TEXT")
    private String ruleConditionsJson;

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getRuleConditionsJson() { return ruleConditionsJson; }
    public void setRuleConditionsJson(String ruleConditionsJson) {
        this.ruleConditionsJson = ruleConditionsJson;
    }
}