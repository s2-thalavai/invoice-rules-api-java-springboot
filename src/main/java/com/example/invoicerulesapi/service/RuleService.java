package com.example.invoicerulesapi.service;

import com.example.invoicerulesapi.model.Rule;
import java.util.ArrayList;
import java.util.List;

public class RuleService {
    private final List<Rule> rules = new ArrayList<>();

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public List<Rule> getAllRules() {
        return rules;
    }
}
