package com.example.invoicerulesapi.rules;

import java.util.List;

public class RuleSet {
    private List<ExpressionRule> discountRules;
    private List<ExpressionRule> taxRules;

    public List<ExpressionRule> getDiscountRules() {
        return discountRules;
    }

    public void setDiscountRules(List<ExpressionRule> discountRules) {
        this.discountRules = discountRules;
    }

    public List<ExpressionRule> getTaxRules() {
        return taxRules;
    }

    public void setTaxRules(List<ExpressionRule> taxRules) {
        this.taxRules = taxRules;
    }
}