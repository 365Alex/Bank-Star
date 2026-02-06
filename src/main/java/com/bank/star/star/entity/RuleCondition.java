package com.bank.star.star.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RULE_CONDITIONS")
public class RuleCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private QueryType query;

    @ElementCollection
    @CollectionTable(name = "RULE_CONDITION_ARGUMENTS",
            joinColumns = @JoinColumn(name = "condition_id"))
    @Column(name = "argument", nullable = false)
    private List<String> arguments = new ArrayList<>();

    @Column(nullable = false)
    private boolean negate;

    public RuleCondition() {}

    public RuleCondition(QueryType query, List<String> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
        this.negate = negate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public QueryType getQuery() { return query; }
    public void setQuery(QueryType query) { this.query = query; }

    public List<String> getArguments() { return arguments; }
    public void setArguments(List<String> arguments) {
        this.arguments = arguments != null ? arguments : new ArrayList<>();
    }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }
}