package com.bank.star.star.repository;

import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    // Кеши для разных запросов
    private final Cache<String, Boolean> hasProductCache;
    private final Cache<String, Long> transactionSumCache;

    public TransactionRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.hasProductCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        this.transactionSumCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public boolean hasProduct(UUID userId, ProductType productType) {
        String cacheKey = userId + "_" + productType;

        return hasProductCache.get(cacheKey, key -> {
            String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM transaction t
                    JOIN product p ON t.product_id = p.id
                    WHERE t.user_id = ? 
                    AND p.type = ?
                    LIMIT 1
                )
                """;

            Boolean result = jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    userId,
                    productType.name()
            );
            return Boolean.TRUE.equals(result);
        });
    }

    public long getTransactionSum(UUID userId, ProductType productType, TransactionType transactionType) {
        String cacheKey = userId + "_" + productType + "_" + transactionType;

        return transactionSumCache.get(cacheKey, key -> {
            String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transaction t
                JOIN product p ON t.product_id = p.id
                WHERE t.user_id = ?
                AND p.type = ?
                AND t.type = ?
                """;

            Long result = jdbcTemplate.queryForObject(
                    sql,
                    Long.class,
                    userId,
                    productType.name(),
                    transactionType.name()
            );
            return result != null ? result : 0L;
        });
    }

    public long getDepositSum(UUID userId, ProductType productType) {
        return getTransactionSum(userId, productType, TransactionType.DEPOSIT);
    }

    public long getWithdrawSum(UUID userId, ProductType productType) {
        return getTransactionSum(userId, productType, TransactionType.WITHDRAW);
    }

    public boolean compareTransactionSums(UUID userId,
                                          ProductType productType1, TransactionType transactionType1,
                                          ProductType productType2, TransactionType transactionType2) {
        long sum1 = getTransactionSum(userId, productType1, transactionType1);
        long sum2 = getTransactionSum(userId, productType2, transactionType2);
        return sum1 > sum2;
    }

    public boolean transactionSumCompare(UUID userId, int threshold,
                                         ProductType productType, TransactionType transactionType) {
        long sumInKopecks = getTransactionSum(userId, productType, transactionType);
        long thresholdInKopecks = threshold * 100L;
        return sumInKopecks > thresholdInKopecks;
    }

    public boolean transactionSumGreaterOrEqual(UUID userId, int threshold,
                                                ProductType productType, TransactionType transactionType) {
        long sumInKopecks = getTransactionSum(userId, productType, transactionType);
        long thresholdInKopecks = threshold * 100L;
        return sumInKopecks >= thresholdInKopecks;
    }
}