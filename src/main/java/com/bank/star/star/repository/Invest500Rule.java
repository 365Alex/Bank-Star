package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class Invest500Rule implements RecommendationRuleSet{
    private static final String PRODUCT_ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final TransactionRepository transactionRepository;

    public Invest500Rule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {
        // Правило 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean usesDebit = transactionRepository.usesProductType(userId, "DEBIT");
        if (!usesDebit) {return Optional.empty();}

        // Правило 2: Пользователь не использует продукты с типом INVEST
        boolean usesInvest = transactionRepository.usesProductType(userId, "INVEST");
        if (usesInvest) {
            return Optional.empty();
        }

        // Правило 3: Сумма пополнений продуктов с типом SAVING больше 1000 ₽
        BigDecimal totalSavingDeposits = transactionRepository.getTotalSavingDeposits(userId);
        if (totalSavingDeposits.compareTo(new BigDecimal("1000")) <= 0) {return Optional.empty();}

        // Все правила выполнены
        return Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }
}
