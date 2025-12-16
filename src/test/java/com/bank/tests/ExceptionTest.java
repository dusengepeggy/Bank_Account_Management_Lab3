package com.bank.tests;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAccountException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AccountManager;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify custom exceptions are thrown correctly.
 */
public class ExceptionTest {
    private AccountManager accountManager;
    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {

        Account.setAccountCounter(0);
        Customer.setCustomerCounter(0);
        

        accountManager = new AccountManager();
        

        customer1 = new RegularCustomer("Test User 1", 25, "111-111-1111", "Test Address 1");
        customer2 = new RegularCustomer("Test User 2", 30, "222-222-2222", "Test Address 2");
        

        checkingAccount = new CheckingAccount(customer1, 1000.0, "Active");
        savingsAccount = new SavingsAccount(customer2, 1000.0, "Active");
        

        accountManager.addAccount(checkingAccount);
        accountManager.addAccount(savingsAccount);
    }

    // ========== InvalidAmountException Tests ==========
    
    @Test
    void testInvalidAmountException_Deposit_NegativeAmount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.deposit(-100.0);
        }, "Deposit with negative amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Invalid amount"),
                "Exception message should mention invalid amount");
    }

    @Test
    void testInvalidAmountException_Deposit_ZeroAmount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.deposit(0.0);
        }, "Deposit with zero amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals(0.0, exception.getAmount(), 0.01,
                "Exception should store the invalid amount");
    }

    @Test
    void testInvalidAmountException_Withdrawal_NegativeAmount_CheckingAccount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.withdraw(-50.0);
        }, "Withdrawal with negative amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals(-50.0, exception.getAmount(), 0.01,
                "Exception should store the invalid amount");
    }

    @Test
    void testInvalidAmountException_Withdrawal_ZeroAmount_CheckingAccount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.withdraw(0.0);
        }, "Withdrawal with zero amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals(0.0, exception.getAmount(), 0.01,
                "Exception should store the invalid amount");
    }

    @Test
    void testInvalidAmountException_Withdrawal_NegativeAmount_SavingsAccount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(-75.0);
        }, "Withdrawal with negative amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals(-75.0, exception.getAmount(), 0.01,
                "Exception should store the invalid amount");
    }

    @Test
    void testInvalidAmountException_Withdrawal_ZeroAmount_SavingsAccount() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(0.0);
        }, "Withdrawal with zero amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals(0.0, exception.getAmount(), 0.01,
                "Exception should store the invalid amount");
    }

    @Test
    void testInvalidAmountException_ProcessTransaction_Deposit() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.processTransaction(-200.0, "DEPOSIT");
        }, "Process transaction with negative deposit amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
    }

    @Test
    void testInvalidAmountException_ProcessTransaction_Withdrawal() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.processTransaction(-150.0, "WITHDRAWAL");
        }, "Process transaction with negative withdrawal amount should throw InvalidAmountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
    }

    // ========== InsufficientFundsException Tests ==========
    
    @Test
    void testInsufficientFundsException_SavingsAccount_ExceedsBalance() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.withdraw(2000.0); // More than current balance of 1000
        }, "Withdrawal exceeding balance should throw InsufficientFundsException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Insufficient funds"),
                "Exception message should mention insufficient funds");
        assertEquals(1000.0, exception.getBalance(), 0.01,
                "Exception should store the current balance");
        assertEquals(2000.0, exception.getRequestedAmount(), 0.01,
                "Exception should store the requested amount");
    }

    @Test
    void testInsufficientFundsException_SavingsAccount_ViolatesMinimumBalance() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.withdraw(600.0);
        }, "Withdrawal violating minimum balance should throw InsufficientFundsException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Insufficient funds"),
                "Exception message should mention insufficient funds");
        assertTrue(exception.getMessage().contains("minimum balance"),
                "Exception message should mention minimum balance");
        assertEquals(1000.0, exception.getBalance(), 0.01,
                "Exception should store the current balance");
        assertEquals(600.0, exception.getRequestedAmount(), 0.01,
                "Exception should store the requested amount");
    }

    @Test
    void testInsufficientFundsException_ProcessTransaction_Withdrawal() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.processTransaction(1500.0, "WITHDRAWAL");
        }, "Process transaction with insufficient funds should throw InsufficientFundsException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
    }

    // ========== OverdraftExceededException Tests ==========
    
    @Test
    void testOverdraftExceededException_CheckingAccount_ExceedsLimit() {
        double withdrawalAmount = checkingAccount.getBalance() + 1001.0;
        
        OverdraftExceededException exception = assertThrows(OverdraftExceededException.class, () -> {
            checkingAccount.withdraw(withdrawalAmount);
        }, "Withdrawal exceeding overdraft limit should throw OverdraftExceededException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Overdraft limit exceeded"),
                "Exception message should mention overdraft limit exceeded");
        assertEquals(1000.0, exception.getCurrentBalance(), 0.01,
                "Exception should store the current balance");
        assertEquals(withdrawalAmount, exception.getRequestedAmount(), 0.01,
                "Exception should store the requested amount");
        assertEquals(1000.0, exception.getOverdraftLimit(), 0.01,
                "Exception should store the overdraft limit");
    }

    @Test
    void testOverdraftExceededException_ProcessTransaction_Withdrawal() {
        double withdrawalAmount = checkingAccount.getBalance() + 1001.0;
        
        OverdraftExceededException exception = assertThrows(OverdraftExceededException.class, () -> {
            checkingAccount.processTransaction(withdrawalAmount, "WITHDRAWAL");
        }, "Process transaction exceeding overdraft limit should throw OverdraftExceededException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
    }

    // ========== InvalidAccountException Tests ==========
    
    @Test
    void testInvalidAccountException_AccountNotFound() {
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () -> {
            accountManager.findAccount("NONEXISTENT123");
        }, "Finding non-existent account should throw InvalidAccountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Invalid account"),
                "Exception message should mention invalid account");
        assertTrue(exception.getMessage().contains("not found"),
                "Exception message should mention account not found");
        assertEquals("NONEXISTENT123", exception.getAccountNumber(),
                "Exception should store the invalid account number");
    }

    @Test
    void testInvalidAccountException_EmptyAccountNumber() {
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () -> {
            accountManager.findAccount("");
        }, "Finding account with empty number should throw InvalidAccountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertEquals("", exception.getAccountNumber(),
                "Exception should store the empty account number");
    }

    @Test
    void testInvalidAccountException_NullAccountNumber() {
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () -> {
            accountManager.findAccount(null);
        }, "Finding account with null number should throw InvalidAccountException");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertNull(exception.getAccountNumber(),
                "Exception should store null as account number");
    }

    // ========== Exception Message Content Tests ==========
    
    @Test
    void testExceptionMessage_InvalidAmountException_ContainsAmount() {
        try {
            checkingAccount.deposit(-250.0);
            fail("Should have thrown InvalidAmountException");
        } catch (InvalidAmountException e) {
            assertTrue(e.getMessage().contains("$"),
                    "Exception message should contain dollar sign");
            assertTrue(e.getMessage().contains("greater than zero"),
                    "Exception message should mention amount must be greater than zero");
        }
    }

    @Test
    void testExceptionMessage_InsufficientFundsException_ContainsDetails() throws InvalidAmountException {
        try {
            savingsAccount.withdraw(600.0);
            fail("Should have thrown InsufficientFundsException");
        } catch (InsufficientFundsException e) {
            assertTrue(e.getMessage().contains("Current balance"),
                    "Exception message should contain current balance");
            assertTrue(e.getMessage().contains("withdrawal"),
                    "Exception message should mention withdrawal");
        }
    }

    @Test
    void testExceptionMessage_OverdraftExceededException_ContainsDetails() throws InvalidAmountException {
        try {
            checkingAccount.withdraw(checkingAccount.getBalance() + 1001.0);
            fail("Should have thrown OverdraftExceededException");
        } catch (OverdraftExceededException e) {
            assertTrue(e.getMessage().contains("Current balance"),
                    "Exception message should contain current balance");
            assertTrue(e.getMessage().contains("overdraft limit"),
                    "Exception message should mention overdraft limit");
        }
    }

    @Test
    void testExceptionMessage_InvalidAccountException_ContainsAccountNumber() {
        try {
            accountManager.findAccount("INVALID999");
            fail("Should have thrown InvalidAccountException");
        } catch (InvalidAccountException e) {
            assertTrue(e.getMessage().contains("INVALID999"),
                    "Exception message should contain the account number");
            assertTrue(e.getMessage().contains("not found"),
                    "Exception message should mention account not found");
        }
    }
}

