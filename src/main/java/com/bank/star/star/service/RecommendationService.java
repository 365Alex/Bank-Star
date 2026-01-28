package com.bank.star.star.service;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.model.DynamicRule;
import com.bank.star.star.repository.DynamicRuleRepository;
import com.bank.star.star.repository.RecommendationRuleSet;
import com.bank.star.star.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleStatisticRepository ruleStatisticRepository;
    private final DynamicRuleChecker dynamicRuleChecker;

    public RecommendationService(List<RecommendationRuleSet> staticRules,
                                 DynamicRuleRepository dynamicRuleRepository,
                                 RuleStatisticRepository ruleStatisticRepository,
                                 DynamicRuleChecker dynamicRuleChecker) {
        this.staticRules = staticRules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleStatisticRepository = ruleStatisticRepository;
        this.dynamicRuleChecker = dynamicRuleChecker;
    }

    @Transactional
    public RecommendationResponse getRecommendations(UUID userId) {
        List<ProductRecommendation> recommendations = new ArrayList<>();

        // 1. Проверяем статические правила
        for (RecommendationRuleSet rule : staticRules) {
            Optional<ProductRecommendation> recommendation = rule.check(userId);
            recommendation.ifPresent(recommendations::add);
        }

        // 2. Проверяем динамические правила
        List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRule rule : dynamicRules) {
            if (Boolean.FALSE.equals(rule.getIsActive())) {
                continue;
            }

            try {
                if (dynamicRuleChecker.checkRuleForUser(rule, userId)) {
                    recommendations.add(new ProductRecommendation(
                            rule.getProductName(),
                            rule.getProductId(),
                            rule.getProductText()
                    ));
                    updateRuleStatistic(rule.getId().toString(), rule.getProductName());
                }
            } catch (Exception e) {
                System.err.println("Error checking dynamic rule " + rule.getId() + ": " + e.getMessage());
            }
        }

        return new RecommendationResponse(userId.toString(), recommendations);
    }

    private void updateRuleStatistic(String ruleId, String ruleName) {
        try {
            var statistic = ruleStatisticRepository.findById(ruleId)
                    .orElseGet(() -> {
                        var newStat = new com.bank.star.star.entity.RuleStatistic();
                        newStat.setId(ruleId);
                        newStat.setRuleId(ruleId);
                        newStat.setRuleName(ruleName);
                        newStat.setExecutionCount(0L);
                        return newStat;
                    });

            statistic.incrementCount();
            ruleStatisticRepository.save(statistic);
        } catch (Exception e) {
            System.err.println("Failed to update statistic: " + e.getMessage());
        }
    }
}