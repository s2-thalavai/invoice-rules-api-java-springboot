
package com.example.invoicerulesapi.rules;

public class ExpressionRule {
    private String condition;
    private double rate;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}