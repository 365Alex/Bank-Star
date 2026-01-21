package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String DESCRIPTION = "Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образования, лечения и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final TransactionRepository transactionRepository;

    public SimpleCreditRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {
        boolean isEligible =
                // Правило 1: Не использует кредитные продукты
                !transactionRepository.hasProduct(userId, ProductType.CREDIT)
                        // Правило 2: Пополнения DEBIT больше расходов DEBIT
                        && transactionRepository.compareTransactionSums(
                        userId,
                        ProductType.DEBIT, TransactionType.DEPOSIT,
                        ProductType.DEBIT, TransactionType.WITHDRAW
                )
                        // Правило 3: Расходы DEBIT больше 100 000 ₽
                        && transactionRepository.transactionSumCompare(
                        userId, 100000, ProductType.DEBIT, TransactionType.WITHDRAW
                );

        return isEligible
                ? Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION))
                : Optional.empty();
    }
}