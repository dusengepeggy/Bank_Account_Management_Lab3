package com.bank.tests;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.Transaction;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AccountManager;
import services.TransactionManager;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private CheckingAccount sourceAccount;
    private SavingsAccount destinationAccount;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        Account.setAccountCounter(0);
        Customer.setCustomerCounter(0);

        accountManager = new AccountManager();
        transactionManager = new TransactionManager();

        customer1 = new RegularCustomer("Alice Johnson", 28, "555-0101", "789 Elm St");
        customer2 = new RegularCustomer("Bob Williams", 32, "555-0102", "321 Pine St");

        sourceAccount = new CheckingAccount(customer1, 2000.0, "Active");
        destinationAccount = new SavingsAccount(customer2, 1000.0, "Active");

        accountManager.addAccount(sourceAccount);
        accountManager.addAccount(destinationAccount);
    }

    // ========== Transfer Tests ==========
    
    @Test
    void testTransfer_ValidAmount_BetweenAccounts() 
            throws InvalidAmountException, InsufficientFundsException, 
            OverdraftExceededException {
        double transferAmount = 500.0;
        double sourceInitialBalance = sourceAccount.getBalance();
        double destinationInitialBalance = destinationAccount.getBalance();
        
        // Perform transfer: withdraw from source, deposit to destination
        sourceAccount.withdraw(transferAmount);
        destinationAccount.deposit(transferAmount);
        
        // Record transactions
        Transaction withdrawalTransaction = new Transaction(
            sourceAccount.getAccountNumber(), 
            "Withdrawal", 
            transferAmount, 
            sourceAccount.getBalance()
        );
        Transaction depositTransaction = new Transaction(
            destinationAccount.getAccountNumber(), 
            "Deposit", 
            transferAmount, 
            destinationAccount.getBalance()
        );
        
        transactionManager.addTransaction(withdrawalTransaction);
        transactionManager.addTransaction(depositTransaction);

        assertEquals(sourceInitialBalance - transferAmount, sourceAccount.getBalance(), 0.01,
                "Source account balance should decrease by transfer amount");
        assertEquals(destinationInitialBalance + transferAmount, destinationAccount.getBalance(), 0.01,
                "Destination account balance should increase by transfer amount");

        assertEquals(2, transactionManager.getTransactionCount(),
                "TransactionManager should have 2 transactions recorded");
    }

    @Test
    void testTransfer_MultipleTransfers() 
            throws InvalidAmountException, InsufficientFundsException, 
            OverdraftExceededException {
        double transferAmount1 = 200.0;
        double transferAmount2 = 300.0;

        sourceAccount.withdraw(transferAmount1);
        destinationAccount.deposit(transferAmount1);
        transactionManager.addTransaction(new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount1, sourceAccount.getBalance()));
        transactionManager.addTransaction(new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount1, destinationAccount.getBalance()));

        sourceAccount.withdraw(transferAmount2);
        destinationAccount.deposit(transferAmount2);
        transactionManager.addTransaction(new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount2, sourceAccount.getBalance()));
        transactionManager.addTransaction(new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount2, destinationAccount.getBalance()));

        assertEquals(4, transactionManager.getTransactionCount(),
                "TransactionManager should have 4 transactions recorded");
    }

    @Test
    void testTransfer_UsingOverdraft_CheckingAccount() 
            throws InvalidAmountException, InsufficientFundsException, 
            OverdraftExceededException {
        double transferAmount = sourceAccount.getBalance() + 500.0;
        double sourceInitialBalance = sourceAccount.getBalance();
        double destinationInitialBalance = destinationAccount.getBalance();
        

        sourceAccount.withdraw(transferAmount);
        destinationAccount.deposit(transferAmount);

        transactionManager.addTransaction(new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount, sourceAccount.getBalance()));
        transactionManager.addTransaction(new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount, destinationAccount.getBalance()));

        assertEquals(sourceInitialBalance - transferAmount, sourceAccount.getBalance(), 0.01,
                "Source account should have negative balance within overdraft limit");
        assertEquals(destinationInitialBalance + transferAmount, destinationAccount.getBalance(), 0.01,
                "Destination account balance should increase");
    }

    @Test
    void testTransfer_FilterTransactionsByAccount() 
            throws InvalidAmountException, InsufficientFundsException, 
            OverdraftExceededException {
        double transferAmount = 400.0;
        

        sourceAccount.withdraw(transferAmount);
        destinationAccount.deposit(transferAmount);
        
        Transaction withdrawalTransaction = new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount, sourceAccount.getBalance());
        Transaction depositTransaction = new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount, destinationAccount.getBalance());
        
        transactionManager.addTransaction(withdrawalTransaction);
        transactionManager.addTransaction(depositTransaction);

        List<Transaction> sourceTransactions = transactionManager.filterById(sourceAccount.getAccountNumber());
        assertEquals(1, sourceTransactions.size(),
                "Should find 1 transaction for source account");
        assertEquals("Withdrawal", sourceTransactions.get(0).getType(),
                "Transaction type should be Withdrawal");

        List<Transaction> destinationTransactions = transactionManager.filterById(destinationAccount.getAccountNumber());
        assertEquals(1, destinationTransactions.size(),
                "Should find 1 transaction for destination account");
        assertEquals("Deposit", destinationTransactions.get(0).getType(),
                "Transaction type should be Deposit");
    }

    @Test
    void testTransfer_CalculateDepositsAndWithdrawals() 
            throws InvalidAmountException, InsufficientFundsException, 
            OverdraftExceededException {
        double transferAmount1 = 300.0;
        double transferAmount2 = 200.0;

        sourceAccount.withdraw(transferAmount1);
        destinationAccount.deposit(transferAmount1);
        transactionManager.addTransaction(new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount1, sourceAccount.getBalance()));
        transactionManager.addTransaction(new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount1, destinationAccount.getBalance()));
        
        sourceAccount.withdraw(transferAmount2);
        destinationAccount.deposit(transferAmount2);
        transactionManager.addTransaction(new Transaction(
            sourceAccount.getAccountNumber(), "Withdrawal", transferAmount2, sourceAccount.getBalance()));
        transactionManager.addTransaction(new Transaction(
            destinationAccount.getAccountNumber(), "Deposit", transferAmount2, destinationAccount.getBalance()));

        double totalDeposits = transactionManager.calculateDeposits(destinationAccount.getAccountNumber());
        assertEquals(transferAmount1 + transferAmount2, totalDeposits, 0.01,
                "Total deposits for destination account should match sum of transfers");

        double totalWithdrawals = transactionManager.calculateWithdrawal(sourceAccount.getAccountNumber());
        assertEquals(transferAmount1 + transferAmount2, totalWithdrawals, 0.01,
                "Total withdrawals for source account should match sum of transfers");
    }

    @Test
    void testTransfer_InvalidAccountNumber_FilterReturnsEmpty() {
        Transaction transaction = new Transaction(
            sourceAccount.getAccountNumber(), "Deposit", 100.0, 1100.0);
        transactionManager.addTransaction(transaction);

        List<Transaction> result = transactionManager.filterById("INVALID123");
        assertEquals(0, result.size(),
                "Filtering with invalid account number should return empty list");
    }

    @Test
    void testTransfer_NullAccountNumber_FilterReturnsEmpty() {
        Transaction transaction = new Transaction(
            sourceAccount.getAccountNumber(), "Deposit", 100.0, 1100.0);
        transactionManager.addTransaction(transaction);

        List<Transaction> result = transactionManager.filterById(null);
        assertEquals(0, result.size(),
                "Filtering with null account number should return empty list");
    }

    @Test
    void testTransfer_EmptyAccountNumber_FilterReturnsEmpty() {
        Transaction transaction = new Transaction(
            sourceAccount.getAccountNumber(), "Deposit", 100.0, 1100.0);
        transactionManager.addTransaction(transaction);

        List<Transaction> result = transactionManager.filterById("");
        assertEquals(0, result.size(),
                "Filtering with empty account number should return empty list");
    }

    @Test
    void testTransfer_AddInvalidTransaction_NotAdded() {
        int initialCount = transactionManager.getTransactionCount();

        transactionManager.addTransaction(null);
        
        assertEquals(initialCount, transactionManager.getTransactionCount(),
                "Null transaction should not be added");
    }
}

