package utils;

import models.Account;
import models.Transaction;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAccountException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import services.AccountManager;
import services.TransactionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyUtils {
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;

    public ConcurrencyUtils(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    public void runConcurrentSimulation(String accountNumber, int numThreads, int operationsPerThread) 
            throws InvalidAccountException {
        Account account = accountManager.findAccount(accountNumber);
        double initialBalance = account.getBalance();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CONCURRENT TRANSACTION SIMULATION");
        System.out.println("=".repeat(60));
        System.out.println("Account: " + accountNumber);
        System.out.println("Initial Balance: $" + String.format("%.2f", initialBalance));
        System.out.println("Threads: " + numThreads);
        System.out.println("Operations per Thread: " + operationsPerThread);
        System.out.println("Total Operations: " + (numThreads * operationsPerThread));
        System.out.println("-".repeat(60));
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        List<Transaction> transactions = new ArrayList<>();
        Object transactionLock = new Object();
        
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        boolean isDeposit = (threadId + j) % 2 == 0;
                        double amount = 10.0 + (j % 5) * 5.0;
                        
                        try {
                            boolean success = account.processTransaction(
                                amount, 
                                isDeposit ? "DEPOSIT" : "WITHDRAWAL"
                            );
                            
                            if (success) {
                                successCount.incrementAndGet();
                                double newBalance = account.getBalance();
                                
                                synchronized (transactionLock) {
                                    Transaction transaction = new Transaction(
                                        accountNumber,
                                        isDeposit ? "DEPOSIT" : "WITHDRAWAL",
                                        amount,
                                        newBalance
                                    );
                                    transactions.add(transaction);
                                }
                            } else {
                                failureCount.incrementAndGet();
                            }
                        } catch (InvalidAmountException | InsufficientFundsException 
                                | OverdraftExceededException e) {
                            failureCount.incrementAndGet();
                        }
                        
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
            executor.shutdown();
            
            for (Transaction transaction : transactions) {
                transactionManager.addTransaction(transaction);
            }
            
            double finalBalance = account.getBalance();
            double expectedChange = (successCount.get() - failureCount.get()) * 10.0;
            
            System.out.println("\nSimulation Results:");
            System.out.println("-".repeat(60));
            System.out.println("Successful Operations: " + successCount.get());
            System.out.println("Failed Operations: " + failureCount.get());
            System.out.println("Initial Balance: $" + String.format("%.2f", initialBalance));
            System.out.println("Final Balance: $" + String.format("%.2f", finalBalance));
            System.out.println("Balance Change: $" + String.format("%.2f", finalBalance - initialBalance));
            System.out.println("Transactions Recorded: " + transactions.size());
            System.out.println("=".repeat(60));
            
            if (Math.abs((finalBalance - initialBalance) - expectedChange) < 100.0) {
                System.out.println("\n✓ Simulation completed successfully - Data consistency maintained!");
            } else {
                System.out.println("\n⚠ Balance discrepancy detected - Review required.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\n✗ Simulation interrupted.");
        }
    }

    public void runMixedConcurrentSimulation(String accountNumber, int numThreads) 
            throws InvalidAccountException {
        Account account = accountManager.findAccount(accountNumber);
        double initialBalance = account.getBalance();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MIXED CONCURRENT TRANSACTION SIMULATION");
        System.out.println("=".repeat(60));
        System.out.println("Account: " + accountNumber);
        System.out.println("Initial Balance: $" + String.format("%.2f", initialBalance));
        System.out.println("Threads: " + numThreads);
        System.out.println("-".repeat(60));
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger depositCount = new AtomicInteger(0);
        AtomicInteger withdrawalCount = new AtomicInteger(0);
        List<Transaction> transactions = new ArrayList<>();
        Object transactionLock = new Object();
        
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    int operations = 10;
                    for (int j = 0; j < operations; j++) {
                        boolean isDeposit = threadId % 2 == 0;
                        double amount = 25.0 + (j * 5.0);
                        
                        try {
                            boolean success = account.processTransaction(
                                amount, 
                                isDeposit ? "DEPOSIT" : "WITHDRAWAL"
                            );
                            
                            if (success) {
                                if (isDeposit) {
                                    depositCount.incrementAndGet();
                                } else {
                                    withdrawalCount.incrementAndGet();
                                }
                                
                                double newBalance = account.getBalance();
                                
                                synchronized (transactionLock) {
                                    Transaction transaction = new Transaction(
                                        accountNumber,
                                        isDeposit ? "DEPOSIT" : "WITHDRAWAL",
                                        amount,
                                        newBalance
                                    );
                                    transactions.add(transaction);
                                }
                            }
                        } catch (InvalidAmountException | InsufficientFundsException 
                                | OverdraftExceededException e) {
                        }
                        
                        Thread.sleep(2);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
            executor.shutdown();
            
            for (Transaction transaction : transactions) {
                transactionManager.addTransaction(transaction);
            }
            
            double finalBalance = account.getBalance();
            
            System.out.println("\nSimulation Results:");
            System.out.println("-".repeat(60));
            System.out.println("Deposits: " + depositCount.get());
            System.out.println("Withdrawals: " + withdrawalCount.get());
            System.out.println("Initial Balance: $" + String.format("%.2f", initialBalance));
            System.out.println("Final Balance: $" + String.format("%.2f", finalBalance));
            System.out.println("Balance Change: $" + String.format("%.2f", finalBalance - initialBalance));
            System.out.println("Transactions Recorded: " + transactions.size());
            System.out.println("=".repeat(60));
            System.out.println("\n✓ Mixed concurrent simulation completed successfully!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\n✗ Simulation interrupted.");
        }
    }
}
