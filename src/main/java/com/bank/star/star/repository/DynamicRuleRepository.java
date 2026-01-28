package com.bank.star.star.repository;

import com.bank.star.star.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
    Optional<DynamicRule> findByProductId(String productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM DynamicRule d WHERE d.productId = ?1")
    void deleteByProductId(String productId);
}