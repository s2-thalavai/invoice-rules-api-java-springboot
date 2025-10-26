package com.example.invoicerulesapi.rules;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ExpressionRuleEngine {
    private final AtomicReference<RuleSet> ruleSetRef = new AtomicReference<>(new RuleSet());
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private final Path configPath;

    public ExpressionRuleEngine(Path configPath) {
        this.configPath = configPath;
        loadRulesFromFile();
        startWatcher();
    }

    public void loadRulesFromFile() {
        try (Reader r = new FileReader(configPath.toFile())) {
            Gson g = new Gson();
            RuleSet rs = g.fromJson(r, RuleSet.class);
            ruleSetRef.set(rs == null ? new RuleSet() : rs);
            System.out.println(" Loaded rules from " + configPath + " at " + new Date());
        } catch (Exception e) {
            System.out.println("Failed to load rules.json: " + e.getMessage());
            ruleSetRef.set(new RuleSet());
        }
    }

    public RuleSet getRuleSet() {
        return ruleSetRef.get();
    }

    public void saveRuleSet(RuleSet rs) {
        try (FileWriter fw = new FileWriter(configPath.toFile())) {
            new Gson().toJson(rs, fw);
            ruleSetRef.set(rs);
            System.out.println("Rules saved to " + configPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startWatcher() {
        Thread t = new Thread(() -> {
            try {
                Path parentDir = configPath.getParent();
                if (parentDir == null) {
                    System.err.println(" Skipping file watcher — rules.json has no parent directory: " + configPath);
                    return;
                }

                WatchService ws = FileSystems.getDefault().newWatchService();
                parentDir.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);

                System.out.println("Watching for changes in " + parentDir.toAbsolutePath());

                while (true) {
                    WatchKey key = ws.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (changed.endsWith(configPath.getFileName())) {
                            System.out.println(" Detected rules.json update — reloading...");
                            loadRulesFromFile();
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                System.err.println("Rule watcher stopped safely: " + e.getMessage());
            }
        }, "rule-watcher");

        t.setDaemon(true);
        t.start();
    }

    private boolean evalCondition(String condition, Map<String, Object> ctx) {
        try {
            for (var e : ctx.entrySet())
                engine.put(e.getKey(), e.getValue());
            Object res = engine.eval(condition);
            return Boolean.TRUE.equals(res);
        } catch (Exception ex) {
            System.out.println("Condition eval error: " + ex.getMessage());
            return false;
        }
    }

    public double getDiscountRate(double subtotal) {
        RuleSet set = ruleSetRef.get();
        if (set == null || set.getDiscountRules() == null)
            return 0.05;
        for (ExpressionRule r : set.getDiscountRules()) {
            if (evalCondition(r.getCondition(), Map.of("subtotal", subtotal)))
                return r.getRate();
        }
        return 0.05;
    }

    public double getTaxRate(String customerName, String region) {
        RuleSet set = ruleSetRef.get();
        String customerType = (customerName != null
                && (customerName.toLowerCase().contains("pvt") || customerName.toLowerCase().contains("ltd")))
                        ? "corporate"
                        : "default";
        if (set == null || set.getTaxRules() == null)
            return 0.08;
        for (ExpressionRule r : set.getTaxRules()) {
            Map<String, Object> ctx = Map.of("customerType", customerType, "region", region);
            if (evalCondition(r.getCondition(), ctx))
                return r.getRate();
        }
        return 0.08;
    }
}
