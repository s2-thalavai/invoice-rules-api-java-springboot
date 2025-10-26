package com.example.invoicerulesapi.controller;

import com.example.invoicerulesapi.model.Invoice;
import com.example.invoicerulesapi.model.LineItem;
import com.example.invoicerulesapi.rules.ExpressionRuleEngine;
import com.example.invoicerulesapi.rules.RuleSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final Map<String, Invoice> store = new ConcurrentHashMap<>();
    private final ExpressionRuleEngine engine = new ExpressionRuleEngine(Paths.get("rules.json"));

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody Invoice payload) {
        // populate defaults
        if (payload.getId() == null || payload.getId().isEmpty())
            payload.setId(UUID.randomUUID().toString());
        if (payload.getIssueDate() == null)
            payload.setIssueDate(LocalDate.now());
        if (payload.getDueDate() == null)
            payload.setDueDate(LocalDate.now().plusDays(7));
        store.put(payload.getId(), payload);
        return ResponseEntity.status(201).body(payload);
    }

    @GetMapping
    public Collection<Invoice> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        Invoice inv = store.get(id);
        return inv == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(inv);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable String id) {
        Invoice inv = store.get(id);
        if (inv == null)
            return ResponseEntity.notFound().build();
        inv.setPaid(true);
        return ResponseEntity.ok(inv);
    }

    // computed totals using rules
    @GetMapping("/{id}/totals")
    public ResponseEntity<?> totals(@PathVariable String id) {
        Invoice inv = store.get(id);
        if (inv == null)
            return ResponseEntity.notFound().build();
        double subtotal = inv.subtotal();
        double discountRate = engine.getDiscountRate(subtotal);
        double taxRate = engine.getTaxRate(inv.getCustomerName(), inv.getRegion());
        double discount = subtotal * discountRate;
        double tax = (subtotal - discount) * taxRate;
        double grand = subtotal - discount + tax;
        Map<String, Object> resp = Map.of(
                "subtotal", subtotal,
                "discountRate", discountRate,
                "taxRate", taxRate,
                "discount", discount,
                "tax", tax,
                "grandTotal", grand);
        return ResponseEntity.ok(resp);
    }
}
