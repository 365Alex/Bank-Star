package com.bank.star.star.repository;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет, использует ли пользователь продукты определенного типа
     */
    public boolean usesProductType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*) > 0 
            FROM transaction t
            JOIN product p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
            LIMIT 1
            """;

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType);
        return result != null && result;
    }

    /**
     * Проверяет, НЕ использует ли пользователь продукты определенного типа
     */
    public boolean doesNotUseProductType(UUID userId, String productType) {
        return !usesProductType(userId, productType);
    }

    /**
     * Сумма пополнений по всем продуктам определенного типа
     */
    public BigDecimal getTotalDepositsByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transaction t
            JOIN product p ON t.product_id = p.id
            WHERE t.user_id = ? 
              AND p.type = ? 
              AND t.type = 'DEPOSIT'
            """;

        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId, productType);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Сумма трат по всем продуктам определенного типа
     */
    public BigDecimal getTotalExpensesByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transaction t
            JOIN product p ON t.product_id = p.id
            WHERE t.user_id = ? 
              AND p.type = ? 
              AND t.type = 'EXPENSE'
            """;

        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId, productType);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Получает общую сумму пополнений по продуктам типа SAVING
     */
    public BigDecimal getTotalSavingDeposits(UUID userId) {
        return getTotalDepositsByProductType(userId, "SAVING");
    }

    /**
     * Получает общую сумму пополнений по продуктам типа DEBIT
     */
    public BigDecimal getTotalDebitDeposits(UUID userId) {
        return getTotalDepositsByProductType(userId, "DEBIT");
    }

    /**
     * Получает общую сумму трат по продуктам типа DEBIT
     */
    public BigDecimal getTotalDebitExpenses(UUID userId) {
        return getTotalExpensesByProductType(userId, "DEBIT");
    }
}
