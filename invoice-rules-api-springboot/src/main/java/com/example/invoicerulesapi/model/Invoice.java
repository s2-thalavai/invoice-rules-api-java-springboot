package com.example.invoicerulesapi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an invoice entity with multiple line items.
 * Used by the Invoice Rules API REST endpoints.
 */
public class Invoice {

    private String id;
    private String invoiceNumber;
    private String customerName;
    private String region; // e.g., "IN", "US"
    private List<LineItem> items = new ArrayList<>();
    private boolean paid = false;
    private LocalDate issueDate;
    private LocalDate dueDate;

    // Constructors
    public Invoice() {
        this.id = UUID.randomUUID().toString();
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(7);
    }

    public Invoice(String invoiceNumber, String customerName, String region) {
        this();
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.region = region;
    }

    // Helper method to calculate subtotal
    public double subtotal() {
        return items.stream().mapToDouble(LineItem::getAmount).sum();
    }

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // --- Utility for logging/debugging ---
    @Override
    public String toString() {
        return String.format("""
                Invoice #%s (%s)
                Customer: %s
                Region: %s
                Items: %d
                Subtotal: %.2f
                Paid: %s
                Issue Date: %s | Due Date: %s
                """,
                invoiceNumber, id, customerName, region,
                items.size(), subtotal(),
                paid ? "YES" : "NO", issueDate, dueDate);
    }
}
