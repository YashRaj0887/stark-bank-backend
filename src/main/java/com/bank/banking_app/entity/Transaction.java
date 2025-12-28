package com.bank.banking_app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique ID for the receipt (e.g., Receipt #1)

    private BigDecimal amount;
    
    private String type; // "DEPOSIT" or "WITHDRAWAL"

    private LocalDateTime timestamp; // When did it happen?

    // THE MAGIC LINK (Foreign Key)
    // Many transactions belong to ONE Account
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Default Constructor (Required by Hibernate)
    public Transaction() {}

    // Constructor for us to use easily
    public Transaction(BigDecimal amount, String type, Account account) {
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.timestamp = LocalDateTime.now(); // Auto-set the current time
    }

    // Getters (So we can read the data later)
    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}