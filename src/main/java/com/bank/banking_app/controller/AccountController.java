package com.bank.banking_app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.banking_app.entity.Account;
import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.service.AccountService;

@RestController // 1. Tells Spring: "This is the place for REST API endpoints"
@RequestMapping("/api/accounts") // 2. The base URL. All requests start here.
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

    // POST Request: Create a new account
    // URL: http://localhost:8080/api/accounts
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        // We receive JSON data -> convert to Java Object -> send to Service
        // NEW: We now pass the PIN (account.getPin()) as the 4th argument!
        return accountService.createAccount(
            account.getAccountNumber(), 
            account.getOwnerName(), 
            account.getBalance(),
            account.getPin() 
        );
    }

    // GET Request: Get account details
    // URL: http://localhost:8080/api/accounts/{id}
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) {
        return accountService.getAccount(id);
    }
    // POST Request: Deposit money
    // URL: http://localhost:8080/api/accounts/{id}/deposit
    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable String id, @RequestBody Map<String, BigDecimal> request) {
        
        // We extract the "amount" from the JSON sent by the user
        BigDecimal amount = request.get("amount");
        
        // We call the Service
        return accountService.deposit(id, amount);
    }
    // POST Request: Withdraw money with Error Handling
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String id, @RequestBody Map<String, String> request) {
        // We read the data as Strings first
        BigDecimal amount = new BigDecimal(request.get("amount"));
        String pin = request.get("pin"); // <--- Get the PIN from JSON

        try {
            // Pass the PIN to the Service
            Account account = accountService.withdraw(id, amount, pin);
            return ResponseEntity.ok(account);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // GET Request: See History
    // URL: http://localhost:8080/api/accounts/{id}/transactions
    @GetMapping("/{id}/transactions")
    public List<Transaction> getHistory(@PathVariable String id) {
        return accountService.getTransactionHistory(id);
    }

    // POST Request: Transfer Money
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody Map<String, String> request) {
        try {
            // Extract data
            String fromId = request.get("fromId");
            String toId = request.get("toId");
            BigDecimal amount = new BigDecimal(request.get("amount"));
            String pin = request.get("pin");

            // Call the Service
            accountService.transfer(fromId, toId, amount, pin);
            
            return ResponseEntity.ok("Transfer Successful!");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}