package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.entity.ProductType;
import com.bank.star.star.entity.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
/**
 * Правило для продукта "Top Saving".
 * Проверяет условия для предложения накопительного счета.
 */
@Component
public class TopSavingRule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final TransactionRepository transactionRepository;

    public TopSavingRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {

        if (!transactionRepository.hasProduct(userId, ProductType.DEBIT)) {
            return Optional.empty();
        }

        boolean isEligible =

                (transactionRepository.transactionSumGreaterOrEqual(
                        userId, 50000, ProductType.DEBIT, TransactionType.DEPOSIT
                ) || transactionRepository.transactionSumGreaterOrEqual(
                        userId, 50000, ProductType.SAVING, TransactionType.DEPOSIT
                ))

                        && transactionRepository.compareTransactionSums(
                        userId,
                        ProductType.DEBIT, TransactionType.DEPOSIT,
                        ProductType.DEBIT, TransactionType.WITHDRAW
                );

        return isEligible
                ? Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION))
                : Optional.empty();
    }
}