package com.bank.star.star.service;


import com.bank.star.star.model.DynamicRecommendation;
import com.bank.star.star.model.DynamicRule;
import org.springframework.stereotype.Service;

@Service
public class DynamicRuleService {

    public boolean checkDynamicRule(DynamicRecommendation dynamicRecommendation){

        for (DynamicRule dynamicRule : dynamicRecommendation.getDynamicRuleSet()){
            switch (dynamicRule.getProductId()){
                case "USER_OF":
                    return false;
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    return false;
                default:
                    throw new RuntimeException();
            }

        }

        return true;
    }
}
