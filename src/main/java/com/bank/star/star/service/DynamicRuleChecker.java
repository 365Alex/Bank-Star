package com.bank.star.star.service;

import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import com.bank.star.star.model.DynamicRule;
import com.bank.star.star.repository.TransactionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DynamicRuleChecker {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public DynamicRuleChecker(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    public boolean checkRuleForUser(DynamicRule rule, UUID userId) {
        try {
            if (rule.getRuleConditionsJson() == null || rule.getRuleConditionsJson().isEmpty()) {
                return false;
            }

            JsonNode conditions = objectMapper.readTree(rule.getRuleConditionsJson());

            for (JsonNode condition : conditions) {
                boolean result = checkCondition(condition, userId);

                boolean negate = condition.has("negate") && condition.get("negate").asBoolean();
                if (negate) {
                    result = !result;
                }

                if (!result) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkCondition(JsonNode condition, UUID userId) {
        try {
            String queryType = condition.get("query").asText();
            JsonNode args = condition.get("arguments");

            switch (queryType) {
                case "USER_OF":
                    return checkUserOf(args, userId);
                case "ACTIVE_USER_OF":
                    return checkActiveUserOf(args, userId);
                case "TRANSACTION_SUM_COMPARE":
                    return checkTransactionSumCompare(args, userId);
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    return checkTransactionSumCompareDepositWithdraw(args, userId);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkUserOf(JsonNode args, UUID userId) {
        if (args.size() < 1) return false;
        String productTypeStr = args.get(0).asText();
        ProductType productType = ProductType.valueOf(productTypeStr);
        return transactionRepository.hasProduct(userId, productType);
    }

    private boolean checkActiveUserOf(JsonNode args, UUID userId) {
        if (args.size() < 1) return false;
        String productTypeStr = args.get(0).asText();
        ProductType productType = ProductType.valueOf(productTypeStr);

        // Активный пользователь = минимум 5 транзакций
        // Нужно реализовать метод для подсчета количества транзакций
        long transactionCount = getTransactionCount(userId, productType);
        return transactionCount >= 5;
    }

    private boolean checkTransactionSumCompare(JsonNode args, UUID userId) {
        if (args.size() < 4) return false;

        String productTypeStr = args.get(0).asText();
        String transactionTypeStr = args.get(1).asText();
        String operator = args.get(2).asText();
        long threshold = args.get(3).asLong() * 100; // В копейки

        ProductType productType = ProductType.valueOf(productTypeStr);
        TransactionType transactionType = TransactionType.valueOf(transactionTypeStr);

        long sum = transactionRepository.getTransactionSum(userId, productType, transactionType);

        return compareValues(sum, threshold, operator);
    }

    private boolean checkTransactionSumCompareDepositWithdraw(JsonNode args, UUID userId) {
        if (args.size() < 2) return false;

        String productTypeStr = args.get(0).asText();
        String operator = args.get(1).asText();

        ProductType productType = ProductType.valueOf(productTypeStr);

        long depositSum = transactionRepository.getDepositSum(userId, productType);
        long withdrawSum = transactionRepository.getWithdrawSum(userId, productType);

        return compareValues(depositSum, withdrawSum, operator);
    }

    private long getTransactionCount(UUID userId, ProductType productType) {
        // Упрощенная реализация - в реальном проекте нужно добавить соответствующий запрос
        String sql = """
            SELECT COUNT(*) FROM transaction t
            JOIN product p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
            """;
        try {
            Long count = transactionRepository.getJdbcTemplate().queryForObject(
                    sql, Long.class, userId, productType.name()
            );
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean compareValues(long value1, long value2, String operator) {
        return switch (operator) {
            case ">" -> value1 > value2;
            case "<" -> value1 < value2;
            case "=" -> value1 == value2;
            case ">=" -> value1 >= value2;
            case "<=" -> value1 <= value2;
            default -> false;
        };
    }
}