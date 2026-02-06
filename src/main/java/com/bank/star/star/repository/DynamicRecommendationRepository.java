package com.bank.star.star.repository;

import com.bank.star.star.model.DynamicRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicRecommendationRepository extends JpaRepository<DynamicRecommendation, String> {
    Optional<DynamicRecommendation> findByName(String name);
    List<DynamicRecommendation> findByDynamicRuleSet_ProductNameContaining(String productName);
}