package com.bank.star.star.model;

import com.bank.star.star.entity.RuleCondition;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "DYNAMIC_RULES")
public class DynamicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "product_text", columnDefinition = "TEXT", nullable = false)
    private String productText;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_id")
    private List<RuleCondition> conditions;

    @Column(name = "conditions_json", columnDefinition = "TEXT")
    private String conditionsJson;

    public DynamicRule() {}

    public DynamicRule(String productName, String productId, String productText,
                       Boolean isActive, List<RuleCondition> conditions) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.isActive = isActive;
        this.conditions = conditions;
    }

    public String getConditionsJson() { return conditionsJson; }
    public void setConditionsJson(String conditionsJson) { this.conditionsJson = conditionsJson; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<RuleCondition> getConditions() { return conditions; }
    public void setConditions(List<RuleCondition> conditions) { this.conditions = conditions; }
}