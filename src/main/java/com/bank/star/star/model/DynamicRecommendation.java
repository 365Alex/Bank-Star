package com.bank.star.star.model;

import jakarta.persistence.*;


import java.util.Set;
@Entity
@Table(name = "dynamic_recommendations")
public class DynamicRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "text")
    private String text;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "recommendation_id")
    private Set<DynamicRule> dynamicRuleSet;

    public DynamicRecommendation() {}

    public DynamicRecommendation(String name, String text, Set<DynamicRule> dynamicRuleSet) {
        this.name = name;
        this.text = text;
        this.dynamicRuleSet = dynamicRuleSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<DynamicRule> getDynamicRuleSet() {
        return dynamicRuleSet;
    }

    public void setDynamicRuleSet(Set<DynamicRule> dynamicRuleSet) {
        this.dynamicRuleSet = dynamicRuleSet;
    }
}