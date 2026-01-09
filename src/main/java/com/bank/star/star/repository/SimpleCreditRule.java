package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SimpleCreditRule implements RecommendationRuleSet{

    private static final String PRODUCT_ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String DESCRIPTION = "Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образования, лечения и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final TransactionRepository transactionRepository;

    public SimpleCreditRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {
        // Правило 1: Пользователь не использует продукты с типом CREDIT
        boolean usesCredit = transactionRepository.usesProductType(userId, "CREDIT");
        if (usesCredit) {
            return Optional.empty();
        }

        BigDecimal totalDebitDeposits = transactionRepository.getTotalDebitDeposits(userId);
        BigDecimal totalDebitExpenses = transactionRepository.getTotalDebitExpenses(userId);

        // Правило 2: Сумма пополнений по DEBIT больше суммы трат по DEBIT
        boolean condition2 = totalDebitDeposits.compareTo(totalDebitExpenses) > 0;
        if (!condition2) {
            return Optional.empty();
        }

        // Правило 3: Сумма трат по DEBIT больше 100 000 ₽
        boolean condition3 = totalDebitExpenses.compareTo(new BigDecimal("100000")) > 0;
        if (!condition3) {
            return Optional.empty();
        }

        return Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }
}
