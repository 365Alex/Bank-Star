package com.bank.star.star.controller;

import com.bank.star.star.DTO.CreateDynamicRuleRequest;
import com.bank.star.star.DTO.RuleStatsResponse;
import com.bank.star.star.entity.RuleStatistic;
import com.bank.star.star.model.DynamicRule;
import com.bank.star.star.repository.DynamicRuleRepository;
import com.bank.star.star.repository.RuleStatisticRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleStatisticRepository ruleStatisticRepository;
    private final ObjectMapper objectMapper;

    public RuleStatsController(DynamicRuleRepository dynamicRuleRepository,
                               RuleStatisticRepository ruleStatisticRepository,
                               ObjectMapper objectMapper) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleStatisticRepository = ruleStatisticRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> createRule(@RequestBody CreateDynamicRuleRequest request)
            throws JsonProcessingException {

        DynamicRule rule = new DynamicRule();
        rule.setProductName(request.getProductName());
        rule.setProductId(request.getProductId());
        rule.setProductText(request.getProductText());
        rule.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        // Сохраняем условия как JSON в отдельное поле
        if (request.getConditions() != null) {
            String conditionsJson = objectMapper.writeValueAsString(request.getConditions());
            rule.setConditionsJson(conditionsJson);
        }

        DynamicRule saved = dynamicRuleRepository.save(rule);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAllRules() {
        List<DynamicRule> rules = dynamicRuleRepository.findAll();
        return ResponseEntity.ok().body(rules);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        // Ищем правило по productId
        dynamicRuleRepository.findByProductId(productId)
                .ifPresent(rule -> {
                    dynamicRuleRepository.delete(rule);
                    // Удаляем статистику при удалении правила
                    ruleStatisticRepository.deleteById(rule.getId().toString());
                });
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<RuleStatsResponse> getStats() {
        List<RuleStatistic> allStats = ruleStatisticRepository.findAll();

        List<RuleStatsResponse.RuleStat> stats = allStats.stream()
                .map(stat -> new RuleStatsResponse.RuleStat(
                        stat.getRuleId(),
                        stat.getExecutionCount()
                ))
                .toList();

        return ResponseEntity.ok(new RuleStatsResponse(stats));
    }
}