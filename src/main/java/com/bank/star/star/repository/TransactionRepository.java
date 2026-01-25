package com.bank.star.star.repository;

import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет, использует ли пользователь продукты определенного типа
     */
    public boolean hasProduct(UUID userId, ProductType productType) {
        String sql = """
            SELECT COUNT(*) > 0
            FROM transaction t
            JOIN product p ON t.product_id = p.id
            WHERE t.user_id = ? 
              AND p.type = ? 
            """;

        Boolean result = jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                userId,
                productType.toString()
        );
        return Boolean.TRUE.equals(result);
    }

    /**
     * Получает сумму транзакций определенного типа для определенного типа продукта
     * @return сумма в копейках
     */
    public long getTransactionSum(UUID userId, ProductType productType, TransactionType transactionType) {
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
                productType.toString(),
                transactionType.toString()
        );
        return result != null ? result : 0L;
    }

    /**
     * Сравнивает сумму двух типов транзакций
     * возвращает true если сумма первых транзакций больше суммы вторых транзакций
     */
    public boolean compareTransactionSums(UUID userId,
                                          ProductType productType1, TransactionType transactionType1,
                                          ProductType productType2, TransactionType transactionType2) {
        long sum1 = getTransactionSum(userId, productType1, transactionType1);
        long sum2 = getTransactionSum(userId, productType2, transactionType2);
        return sum1 > sum2;
    }

    /**
     * Проверяет, что сумма транзакций превышает порог
     * @param threshold порог в рублях (автоматически преобразуется в копейки)
     */
    public boolean transactionSumCompare(UUID userId, int threshold,
                                         ProductType productType, TransactionType transactionType) {
        long sumInKopecks = getTransactionSum(userId, productType, transactionType);
        long thresholdInKopecks = threshold * 100L; // конвертируем рубли в копейки
        return sumInKopecks > thresholdInKopecks;
    }

    /**
     * Проверяет, что сумма транзакций больше или равна порогу
     */
    public boolean transactionSumGreaterOrEqual(UUID userId, int threshold,
                                                ProductType productType, TransactionType transactionType) {
        long sumInKopecks = getTransactionSum(userId, productType, transactionType);
        long thresholdInKopecks = threshold * 100L;
        return sumInKopecks >= thresholdInKopecks;
    }

    /**
     * Получает сумму пополнений для определенного типа продукта
     */
    public long getDepositSum(UUID userId, ProductType productType) {
        return getTransactionSum(userId, productType, TransactionType.DEPOSIT);
    }

    /**
     * Получает сумму снятий для определенного типа продукта
     */
    public long getWithdrawSum(UUID userId, ProductType productType) {
        return getTransactionSum(userId, productType, TransactionType.WITHDRAW);
    }
}