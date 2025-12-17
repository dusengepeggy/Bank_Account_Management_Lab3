package com.bank.tests;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Customer regularCustomer;
    private Customer premiumCustomer;
    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;

    @BeforeEach
    void setUp() {
        Account.setAccountCounter(0);
        Customer.setCustomerCounter(0);

        regularCustomer = new RegularCustomer("John Doe", 30,"test@gmail.com" , "123-456-7890", "123 Main St");
        premiumCustomer = new PremiumCustomer("Jane Smith",35, "test2@gmail.com","987-654-3210", "456 Oak Ave");
        

        checkingAccount = new CheckingAccount(regularCustomer, 1000.0, "Active");
        savingsAccount = new SavingsAccount(premiumCustomer, 1000.0, "Active");
    }

    // ========== Valid Deposit Tests ==========
    
    @Test
    void testValidDeposit_CheckingAccount() throws InvalidAmountException {
        double initialBalance = checkingAccount.getBalance();
        double depositAmount = 500.0;
        
        checkingAccount.deposit(depositAmount);
        
        assertEquals(initialBalance + depositAmount, checkingAccount.getBalance(), 0.01,
                "Balance should increase by deposit amount");
    }

    @Test
    void testValidDeposit_SavingsAccount() throws InvalidAmountException {
        double initialBalance = savingsAccount.getBalance();
        double depositAmount = 300.0;
        
        savingsAccount.deposit(depositAmount);
        
        assertEquals(initialBalance + depositAmount, savingsAccount.getBalance(), 0.01,
                "Balance should increase by deposit amount");
    }

    @Test
    void testMultipleDeposits() throws InvalidAmountException {
        double initialBalance = checkingAccount.getBalance();
        
        checkingAccount.deposit(100.0);
        checkingAccount.deposit(200.0);
        checkingAccount.deposit(50.0);
        
        assertEquals(initialBalance + 350.0, checkingAccount.getBalance(), 0.01,
                "Balance should reflect sum of all deposits");
    }

    // ========== Valid Withdrawal Tests ==========
    
    @Test
    void testValidWithdrawal_CheckingAccount() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        double withdrawalAmount = 300.0;
        
        checkingAccount.withdraw(withdrawalAmount);
        
        assertEquals(initialBalance - withdrawalAmount, checkingAccount.getBalance(), 0.01,
                "Balance should decrease by withdrawal amount");
    }

    @Test
    void testValidWithdrawal_SavingsAccount() throws InvalidAmountException, 
            InsufficientFundsException {
        double initialBalance = savingsAccount.getBalance();
        double withdrawalAmount = 400.0;
        
        savingsAccount.withdraw(withdrawalAmount);
        
        assertEquals(initialBalance - withdrawalAmount, savingsAccount.getBalance(), 0.01,
                "Balance should decrease by withdrawal amount");
    }

    @Test
    void testMultipleWithdrawals() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        
        checkingAccount.withdraw(100.0);
        checkingAccount.withdraw(200.0);
        
        assertEquals(initialBalance - 300.0, checkingAccount.getBalance(), 0.01,
                "Balance should reflect sum of all withdrawals");
    }

    // ========== Invalid Amount Tests (Negative) ==========
    
    @Test
    void testDeposit_NegativeAmount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.deposit(-100.0);
        }, "Deposit with negative amount should throw InvalidAmountException");
    }

    @Test
    void testDeposit_ZeroAmount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.deposit(0.0);
        }, "Deposit with zero amount should throw InvalidAmountException");
    }

    @Test
    void testWithdrawal_NegativeAmount_CheckingAccount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.withdraw(-50.0);
        }, "Withdrawal with negative amount should throw InvalidAmountException");
    }

    @Test
    void testWithdrawal_ZeroAmount_CheckingAccount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.withdraw(0.0);
        }, "Withdrawal with zero amount should throw InvalidAmountException");
    }

    @Test
    void testWithdrawal_NegativeAmount_SavingsAccount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(-50.0);
        }, "Withdrawal with negative amount should throw InvalidAmountException");
    }

    @Test
    void testWithdrawal_ZeroAmount_SavingsAccount_ThrowsException() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(0.0);
        }, "Withdrawal with zero amount should throw InvalidAmountException");
    }

    // ========== Insufficient Funds Tests ==========
    
    @Test
    void testWithdrawal_InsufficientFunds_SavingsAccount_ThrowsException() {
        double withdrawalAmount = savingsAccount.getBalance() + 100.0;
        
        assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.withdraw(withdrawalAmount);
        }, "Withdrawal exceeding balance should throw InsufficientFundsException");
    }

    @Test
    void testWithdrawal_ViolatesMinimumBalance_SavingsAccount_ThrowsException() {
        double withdrawalAmount = 600.0;
        
        assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.withdraw(withdrawalAmount);
        }, "Withdrawal violating minimum balance should throw InsufficientFundsException");
    }

    // ========== Overdraft Tests ==========
    
    @Test
    void testWithdrawal_WithinOverdraftLimit_CheckingAccount_Succeeds() 
            throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        double withdrawalAmount = checkingAccount.getBalance() + 500.0;
        
        checkingAccount.withdraw(withdrawalAmount);
        
        assertEquals(-500.0, checkingAccount.getBalance(), 0.01,
                "Balance should be negative but within overdraft limit");
    }

    @Test
    void testWithdrawal_ExceedsOverdraftLimit_CheckingAccount_ThrowsException() {

        double withdrawalAmount = checkingAccount.getBalance() + 1001.0; // Exceeds limit
        
        assertThrows(OverdraftExceededException.class, () -> {
            checkingAccount.withdraw(withdrawalAmount);
        }, "Withdrawal exceeding overdraft limit should throw OverdraftExceededException");
    }

    @Test
    void testWithdrawal_AtOverdraftLimit_CheckingAccount_Succeeds() 
            throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        double withdrawalAmount = checkingAccount.getBalance() + 1000.0;
        
        checkingAccount.withdraw(withdrawalAmount);
        
        assertEquals(-1000.0, checkingAccount.getBalance(), 0.01,
                "Balance should be exactly at overdraft limit");
    }

    // ========== Balance Update Tests ==========
    
    @Test
    void testBalanceUpdate_AfterDepositAndWithdrawal() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        
        checkingAccount.deposit(500.0);
        checkingAccount.withdraw(200.0);
        
        assertEquals(initialBalance + 300.0, checkingAccount.getBalance(), 0.01,
                "Balance should reflect net change from deposit and withdrawal");
    }

    @Test
    void testBalanceUpdate_MultipleTransactions() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        
        checkingAccount.deposit(100.0);
        checkingAccount.withdraw(50.0);
        checkingAccount.deposit(200.0);
        checkingAccount.withdraw(75.0);
        
        assertEquals(initialBalance + 175.0, checkingAccount.getBalance(), 0.01,
                "Balance should reflect all transaction changes");
    }

    @Test
    void testBalanceUpdate_ProcessTransaction_Deposit() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        double depositAmount = 250.0;
        
        boolean result = checkingAccount.processTransaction(depositAmount, "DEPOSIT");
        
        assertTrue(result, "Process transaction should return true for valid deposit");
        assertEquals(initialBalance + depositAmount, checkingAccount.getBalance(), 0.01,
                "Balance should be updated after processing deposit transaction");
    }

    @Test
    void testBalanceUpdate_ProcessTransaction_Withdrawal() throws InvalidAmountException, 
            InsufficientFundsException, OverdraftExceededException {
        double initialBalance = checkingAccount.getBalance();
        double withdrawalAmount = 300.0;
        
        boolean result = checkingAccount.processTransaction(withdrawalAmount, "WITHDRAWAL");
        
        assertTrue(result, "Process transaction should return true for valid withdrawal");
        assertEquals(initialBalance - withdrawalAmount, checkingAccount.getBalance(), 0.01,
                "Balance should be updated after processing withdrawal transaction");
    }
}

