package com.bank.star.star.controller;

import com.bank.star.star.entity.QueryType;
import com.bank.star.star.model.DynamicRule;
import com.bank.star.star.repository.DynamicRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dynamic-rules")
public class DynamicRuleController {

    private final DynamicRuleRepository dynamicRuleRepository;

    public DynamicRuleController(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    @GetMapping
    public List<DynamicRule> getAllRules() {
        return dynamicRuleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DynamicRule> getRuleById(@PathVariable UUID id) {
        return dynamicRuleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<DynamicRule> getRuleByProductId(@PathVariable String productId) {
        return dynamicRuleRepository.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DynamicRule createRule(@RequestBody DynamicRule rule) {
        return dynamicRuleRepository.save(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DynamicRule> updateRule(@PathVariable UUID id,
                                                  @RequestBody DynamicRule updatedRule) {
        return dynamicRuleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setProductName(updatedRule.getProductName());
                    existingRule.setProductId(updatedRule.getProductId());
                    existingRule.setProductText(updatedRule.getProductText());
                    existingRule.setIsActive(updatedRule.getIsActive());
                    existingRule.setConditions(updatedRule.getConditions());
                    return ResponseEntity.ok(dynamicRuleRepository.save(existingRule));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        if (dynamicRuleRepository.existsById(id)) {
            dynamicRuleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DynamicRule> activateRule(@PathVariable UUID id) {
        return toggleRuleActive(id, true);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DynamicRule> deactivateRule(@PathVariable UUID id) {
        return toggleRuleActive(id, false);
    }

    @GetMapping("/query-types")
    public List<QueryType> getQueryTypes() {
        return Arrays.asList(QueryType.values());
    }

    private ResponseEntity<DynamicRule> toggleRuleActive(UUID id, boolean active) {
        return dynamicRuleRepository.findById(id)
                .map(rule -> {
                    rule.setIsActive(active);
                    return ResponseEntity.ok(dynamicRuleRepository.save(rule));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}