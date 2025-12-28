package com.bank.banking_app.entity;

import java.math.BigDecimal; // Imports the tools to talk to DB

import jakarta.persistence.Column;  // Money math
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 1. Tells Spring: "This class equals a Table in MySQL"
@Table(name = "accounts") // 2. Specifically, the 'accounts' table
public class Account {

    @Id // 3. This is the Primary Key
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "balance")
    private BigDecimal balance;

    // NEW SECURITY FIELD
    private String pin;

    // --- CONSTRUCTORS ---
    // Spring Boot needs an empty constructor to work properly
    public Account() {}

    public Account(String accountNumber, String ownerName, BigDecimal balance, String pin ) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.pin = pin; // <--- Save the PIN
    }

    // --- GETTERS AND SETTERS ---
    // (We need these so Spring can read/write the data)

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}