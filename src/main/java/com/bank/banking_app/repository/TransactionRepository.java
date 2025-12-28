package com.bank.banking_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.banking_app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Spring Magic: It reads "findByAccount_Id" and writes the SQL:
    // "SELECT * FROM transactions WHERE account_id = ?"
    // We tell Spring: "Find by Account... then inside that, find by AccountNumber"
       List<Transaction> findByAccountAccountNumber(String accountNumber);
    
}