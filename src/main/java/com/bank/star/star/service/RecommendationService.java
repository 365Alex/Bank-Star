package com.bank.star.star.service;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.entity.RuleStatistic;
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
    private final DynamicRuleChecker dynamicRuleChecker;
    private final RuleStatisticRepository ruleStatisticRepository;

    public RecommendationService(List<RecommendationRuleSet> staticRules,
                                 DynamicRuleRepository dynamicRuleRepository,
                                 DynamicRuleChecker dynamicRuleChecker,
                                 RuleStatisticRepository ruleStatisticRepository) {
        this.staticRules = staticRules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleChecker = dynamicRuleChecker;
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @Transactional
    public RecommendationResponse getRecommendations(UUID userId) {
        List<ProductRecommendation> recommendations = new ArrayList<>();

        // 1. Проверяем статические правила
        for (RecommendationRuleSet rule : staticRules) {
            Optional<ProductRecommendation> recommendation = rule.check(userId);
            if (recommendation.isPresent()) {
                recommendations.add(recommendation.get());
                updateRuleStatistic(getRuleId(rule), rule.getClass().getSimpleName());
            }
        }

        // 2. Проверяем динамические правила
        List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRule rule : dynamicRules) {
            // Пропускаем неактивные правила
            if (rule.getIsActive() != null && !rule.getIsActive()) {
                continue;
            }

            if (dynamicRuleChecker.checkRuleForUser(rule, userId)) {
                recommendations.add(createProductRecommendation(rule));
                updateRuleStatistic(rule.getProductId(), "DynamicRule: " + rule.getProductName());
            }
        }

        return new RecommendationResponse(userId.toString(), recommendations);
    }

    private ProductRecommendation createProductRecommendation(DynamicRule rule) {
        return new ProductRecommendation(
                rule.getProductName(),
                rule.getProductId(),
                rule.getProductText()
        );
    }

    private String getRuleId(RecommendationRuleSet rule) {
        if (rule instanceof com.bank.star.star.repository.Invest500Rule) {
            return "147f6a0f-3b91-413b-ab99-87f081d60d5a";
        } else if (rule instanceof com.bank.star.star.repository.SimpleCreditRule) {
            return "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
        } else if (rule instanceof com.bank.star.star.repository.TopSavingRule) {
            return "59efc529-2fff-41af-baff-90ccd7402925";
        }
        return null;
    }

    private void updateRuleStatistic(String ruleId, String ruleName) {
        if (ruleId == null) return;

        RuleStatistic statistic = ruleStatisticRepository.findByRuleId(ruleId)
                .orElse(new RuleStatistic(ruleId, ruleName));

        statistic.incrementCount();
        ruleStatisticRepository.save(statistic);
    }
}