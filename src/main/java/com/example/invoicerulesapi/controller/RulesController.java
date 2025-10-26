package com.example.invoicerulesapi.controller;

import com.example.invoicerulesapi.rules.ExpressionRuleEngine;
import com.example.invoicerulesapi.rules.RuleSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;

@RestController
@RequestMapping("/api/rules")
public class RulesController {
    private final ExpressionRuleEngine engine;

    public RulesController() {
        this.engine = new ExpressionRuleEngine(Paths.get("rules.json"));
    }

    @GetMapping
    public RuleSet getRules() {
        return engine.getRuleSet();
    }

    @PostMapping
    public ResponseEntity<?> setRules(@RequestBody RuleSet newRules) {
        engine.saveRuleSet(newRules);
        return ResponseEntity.ok().body("Rules updated");
    }
}