package com.bank.star.star.model;

import java.util.Set;

public class DynamicRecommendation {
    private String name;
    private String id;
    private String text;

//   @ OneToMany
    private Set<DynamicRule> dynamicRuleSet;

    public DynamicRecommendation(String name, String id, String text, Set<DynamicRule> dynamicRuleSet) {
        this.name = name;
        this.id = id;
        this.text = text;
        this.dynamicRuleSet = dynamicRuleSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
