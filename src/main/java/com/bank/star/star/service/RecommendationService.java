package com.bank.star.star.service;


import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.repository.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> rules;

    public RecommendationService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public RecommendationResponse getRecommendations(UUID userId) {
        List<ProductRecommendation> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : rules) {
            Optional<ProductRecommendation> recommendation = rule.check(userId);
            recommendation.ifPresent(recommendations::add);
        }

        return new RecommendationResponse(userId.toString(), recommendations);
    }
}
