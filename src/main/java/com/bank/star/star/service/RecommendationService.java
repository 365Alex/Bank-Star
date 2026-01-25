package com.bank.star.star.service;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.entity.RuleStatistic;
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
    private final List<RecommendationRuleSet> rules;
    private final RuleStatisticRepository ruleStatisticRepository;

    public RecommendationService(List<RecommendationRuleSet> rules,
                                 RuleStatisticRepository ruleStatisticRepository) {
        this.rules = rules;
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @Transactional
    public RecommendationResponse getRecommendations(UUID userId) {
        List<ProductRecommendation> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : rules) {
            Optional<ProductRecommendation> recommendation = rule.check(userId);
            if (recommendation.isPresent()) {
                recommendations.add(recommendation.get());

                // Обновляем статистику для статических правил
                String ruleId = getRuleId(rule);
                if (ruleId != null) {
                    updateRuleStatistic(ruleId, rule.getClass().getSimpleName());
                }
            }
        }

        return new RecommendationResponse(userId.toString(), recommendations);
    }

    private String getRuleId(RecommendationRuleSet rule) {
        // Получаем ID правила на основе его типа
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
        RuleStatistic statistic = ruleStatisticRepository.findByRuleId(ruleId)
                .orElse(new RuleStatistic(ruleId, ruleName));

        statistic.incrementCount();
        ruleStatisticRepository.save(statistic);
    }
}