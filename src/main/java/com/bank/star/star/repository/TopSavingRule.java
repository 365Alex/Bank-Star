package com.bank.star.star.repository;

import com.bank.star.star.DTO.ProductRecommendation;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class TopSavingRule implements RecommendationRuleSet{

    private static final String PRODUCT_ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final TransactionRepository transactionRepository;

    public TopSavingRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ProductRecommendation> check(UUID userId) {
        // Правило 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean usesDebit = transactionRepository.usesProductType(userId, "DEBIT");
        if (!usesDebit) {
            return Optional.empty();
        }

        BigDecimal totalDebitDeposits = transactionRepository.getTotalDebitDeposits(userId);
        BigDecimal totalSavingDeposits = transactionRepository.getTotalSavingDeposits(userId);
        BigDecimal totalDebitExpenses = transactionRepository.getTotalDebitExpenses(userId);

        // Правило 2: Сумма пополнений по DEBIT >= 50 000 ИЛИ Сумма пополнений по SAVING >= 50 000
        boolean condition2 = totalDebitDeposits.compareTo(new BigDecimal("50000")) >= 0 ||
                totalSavingDeposits.compareTo(new BigDecimal("50000")) >= 0;
        if (!condition2) {
            return Optional.empty();
        }

        // Правило 3: Сумма пополнений по DEBIT больше суммы трат по DEBIT
        boolean condition3 = totalDebitDeposits.compareTo(totalDebitExpenses) > 0;
        if (!condition3) {
            return Optional.empty();
        }

        return Optional.of(new ProductRecommendation(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }

}
