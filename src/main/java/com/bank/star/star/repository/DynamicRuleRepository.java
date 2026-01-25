package com.bank.star.star.repository;




import com.bank.star.star.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
    Optional<DynamicRule> findByProductId(String productId);
}