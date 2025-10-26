package com.example.invoicerulesapi.model;

public class Rule {
    private String id;
    private String name;
    private String condition;
    private String action;

    public Rule(String id, String name, String condition, String action) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.action = action;
    }

    // Getters and setters omitted for brevity
    public String getName() {
        return name;
    }
}
