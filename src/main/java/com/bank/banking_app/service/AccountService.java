package com.bank.banking_app.service;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.banking_app.entity.Account;
import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.repository.AccountRepository;
import com.bank.banking_app.repository.TransactionRepository; 

@Service // 1. Tells Spring: "This class holds the business logic"
public class AccountService {

    @Autowired // 2. Tells Spring: "Give me the Repository automatically"
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Feature 1: Create a new account
    // Updated Create Method (Now requires PIN)
    public Account createAccount(String accountNumber, String ownerName, BigDecimal initialBalance, String pin) {
        
        // 1. Validation: Check if PIN is valid
        if (pin == null || pin.length() != 4) {
            throw new RuntimeException("PIN must be 4 digits");
        }

        // 2. Create the Account with the PIN
        // NOTE: Make sure your Account.java constructor matches this!
        Account account = new Account(accountNumber, ownerName, initialBalance, pin);
        
        return accountRepository.save(account);
    }

    // Feature 2: Find an account by ID
    public Account getAccount(String accountNumber) {
        // "Optional" is a container that might be empty if no account is found
        return accountRepository.findById(accountNumber).orElse(null);
    }
    // Feature 3: Deposit Money (Now with Receipts!)
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findById(accountNumber).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        // 1. Update the Balance
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // 2. Create the Receipt
        Transaction transaction = new Transaction(amount, "DEPOSIT", account);        
        // 3. Save the Receipt
        transactionRepository.save(transaction);

        return account;
    }
   // Feature 4: Withdraw Money (Now SECURED with PIN)
    public Account withdraw(String accountNumber, BigDecimal amount, String pin) { // <--- Added PIN here
        
        // 1. Find the Account
        Account account = accountRepository.findById(accountNumber).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        // 2. SECURITY CHECK: Does the PIN match?
        if (!account.getPin().equals(pin)) {
            throw new RuntimeException("Invalid PIN"); // <--- Stop right here if wrong!
        }

        // 3. Balance Check
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Funds");
        }

        // 4. Update Balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // 5. Create Receipt
        Transaction transaction = new Transaction(amount, "WITHDRAWAL", account);
        transactionRepository.save(transaction);

        return account;
    }

    public List<Transaction> getTransactionHistory(String accountId) {
        return transactionRepository.findByAccountAccountNumber(accountId);
    }
    // Feature 5: Transfer Money (Sender -> Receiver)
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String pin) {

        // 1. Validation: Prevent Self-Transfer
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new RuntimeException("You cannot transfer money to yourself.");
        }
        
        // 1. Find both accounts
        Account sender = accountRepository.findById(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        
        Account receiver = accountRepository.findById(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // 2. Validate Sender (PIN & Balance)
        if (!sender.getPin().equals(pin)) {
            throw new RuntimeException("Invalid PIN");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Funds");
        }

        // 3. Move the Money
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // 4. Save Changes to Database
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // 5. Create Receipts for BOTH sides
        Transaction debitReceipt = new Transaction(amount, "SENT to " + toAccountNumber, sender);
        Transaction creditReceipt = new Transaction(amount, "RECEIVED from " + fromAccountNumber, receiver);
        
        transactionRepository.save(debitReceipt);
        transactionRepository.save(creditReceipt);
    }
    
}