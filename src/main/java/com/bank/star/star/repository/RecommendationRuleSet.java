package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<ProductRecommendation> check (UUID userId);
}
