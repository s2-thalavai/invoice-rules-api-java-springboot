package com.example.invoicerulesapi;

import com.example.invoicerulesapi.model.Rule;
import com.example.invoicerulesapi.service.RuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @InjectMocks
    private RuleService ruleService;

    @Test
    void shouldAddRule() {
        Rule rule = new Rule("123", "Late Fee", "invoice.daysOverdue > 30", "applyFee(50)");
        ruleService.addRule(rule);
        List<Rule> rules = ruleService.getAllRules();
        assertEquals(1, rules.size());
        assertEquals("Late Fee", rules.get(0).getName());
    }
}