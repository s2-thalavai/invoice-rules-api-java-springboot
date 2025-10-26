package com.example.invoicerulesapi.model;

public class LineItem {
    private String description;
    private double amount;

    public LineItem() {
    }

    public LineItem(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
