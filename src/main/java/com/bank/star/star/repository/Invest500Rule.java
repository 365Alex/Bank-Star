package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденции. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final TransactionRepository transactionRepository;

    public Invest500Rule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {
        boolean isEligible = transactionRepository.hasProduct(userId, ProductType.DEBIT)
                && !transactionRepository.hasProduct(userId, ProductType.INVEST)
                && transactionRepository.transactionSumCompare(userId, 1000, ProductType.SAVING, TransactionType.DEPOSIT);
        if (isEligible) {
            return Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
        }
        return Optional.empty();
    }
}