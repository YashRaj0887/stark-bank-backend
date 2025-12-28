package com.bank.banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.banking_app.entity.Account;

// This acts as our direct line to the database table
public interface AccountRepository extends JpaRepository<Account, String> {
    // Believe it or not, we don't need to write any code here!
    // Spring automatically gives us methods like:
    // .save(), .findById(), .findAll(), .delete()
} 