package com.bank.star.star.service;

import com.bank.star.star.entity.QueryType;
import com.bank.star.star.entity.RuleCondition;
import com.bank.star.star.model.DynamicRule;
import com.bank.star.star.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DynamicRuleChecker {

    private final TransactionRepository transactionRepository;

    public DynamicRuleChecker(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Проверяет выполнение одного динамического правила для пользователя
     */
    public boolean checkRuleForUser(DynamicRule rule, UUID userId) {
        if (rule.getConditions() == null || rule.getConditions().isEmpty()) {
            return false; // Правило без условий не выполняется
        }

        boolean allConditionsMet = true;

        for (RuleCondition condition : rule.getConditions()) {
            boolean conditionResult = checkCondition(condition, userId);

            // Если условие с negate=true, инвертируем результат
            if (condition.isNegate()) {
                conditionResult = !conditionResult;
            }

            if (!conditionResult) {
                allConditionsMet = false;
                break;
            }
        }

        return allConditionsMet;
    }

    /**
     * Проверяет одно условие для пользователя
     */
    private boolean checkCondition(RuleCondition condition, UUID userId) {
        QueryType query = condition.getQuery();
        List<String> arguments = condition.getArguments();

        switch (query) {
            case USER_OF:
                return checkUserOfCondition(arguments, userId);

            case ACTIVE_USER_OF:
                return checkActiveUserOfCondition(arguments, userId);

            case TRANSACTION_SUM_COMPARE:
                return checkTransactionSumCompare(arguments, userId);

            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW:
                return checkTransactionSumCompareDepositWithdraw(arguments, userId);

            default:
                throw new IllegalArgumentException("Unknown query type: " + query);
        }
    }

    /**
     * Проверяет условие USER_OF
     * arguments[0] - тип продукта (DEBIT, CREDIT, etc.)
     */
    private boolean checkUserOfCondition(List<String> arguments, UUID userId) {
        if (arguments.size() < 1) return false;

        try {
            com.bank.star.star.entity.ProductType productType =
                    com.bank.star.star.entity.ProductType.valueOf(arguments.get(0));
            return transactionRepository.hasProduct(userId, productType);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Проверяет условие ACTIVE_USER_OF
     * arguments[0] - тип продукта
     * arguments[1] - минимальная сумма транзакций (в рублях)
     */
    private boolean checkActiveUserOfCondition(List<String> arguments, UUID userId) {
        if (arguments.size() < 2) return false;

        try {
            com.bank.star.star.entity.ProductType productType =
                    com.bank.star.star.entity.ProductType.valueOf(arguments.get(0));
            int minAmount = Integer.parseInt(arguments.get(1));

            // Проверяем, что сумма всех транзакций по продукту > minAmount
            long depositSum = transactionRepository.getDepositSum(userId, productType);
            long withdrawSum = transactionRepository.getWithdrawSum(userId, productType);
            long totalSum = depositSum + withdrawSum;

            // Конвертируем рубли в копейки для сравнения
            return totalSum > (minAmount * 100L);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет условие TRANSACTION_SUM_COMPARE
     * arguments[0] - тип продукта
     * arguments[1] - тип транзакции (DEPOSIT/WITHDRAW)
     * arguments[2] - пороговая сумма (в рублях)
     */
    private boolean checkTransactionSumCompare(List<String> arguments, UUID userId) {
        if (arguments.size() < 3) return false;

        try {
            com.bank.star.star.entity.ProductType productType =
                    com.bank.star.star.entity.ProductType.valueOf(arguments.get(0));
            com.bank.star.star.entity.TransactionType transactionType =
                    com.bank.star.star.entity.TransactionType.valueOf(arguments.get(1));
            int threshold = Integer.parseInt(arguments.get(2));

            return transactionRepository.transactionSumCompare(userId, threshold,
                    productType, transactionType);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет условие TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
     * arguments[0] - тип продукта
     * Сравнивает сумму пополнений и снятий
     */
    private boolean checkTransactionSumCompareDepositWithdraw(List<String> arguments, UUID userId) {
        if (arguments.size() < 1) return false;

        try {
            com.bank.star.star.entity.ProductType productType =
                    com.bank.star.star.entity.ProductType.valueOf(arguments.get(0));

            // Сравниваем сумму пополнений и снятий
            return transactionRepository.compareTransactionSums(
                    userId,
                    productType, com.bank.star.star.entity.TransactionType.DEPOSIT,
                    productType, com.bank.star.star.entity.TransactionType.WITHDRAW
            );
        } catch (Exception e) {
            return false;
        }
    }
}