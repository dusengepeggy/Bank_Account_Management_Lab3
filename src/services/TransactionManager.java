package services;

import models.Account;
import models.Transaction;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAccountException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages transactions for bank accounts.
 * Provides functionality to add, filter, and calculate transaction statistics.
 */
public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Adds a new transaction to the manager.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(Transaction transaction) {
        if (validateTransaction(transaction)) {
            transactions.add(transaction);
        }
    }

    /**
     * Validates a transaction before adding it to the manager.
     *
     * @param transaction the transaction to validate
     * @return true if the transaction is valid, false otherwise
     */
    private boolean validateTransaction(Transaction transaction) {
        return transaction != null
                && transaction.getAccountNumber() != null
                && !transaction.getAccountNumber().isEmpty()
                && transaction.getAmount() > 0;
    }
    public List<Transaction> filterById(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return new ArrayList<>();
        }

        return transactions.stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    
    /**
     * Displays all transactions for a specific account along with summary statistics.
     *
     * @param accountNumber the account number to view transactions for
     */
    public void viewTransactionsByAccounts(String accountNumber) {
        List<Transaction> transactionsByAccountId = filterById(accountNumber);
        if (transactionsByAccountId.isEmpty()) {
            System.out.println("No transactions yet");
            return;
        }

        displayTransactionList(transactionsByAccountId);
        displayTransactionSummary(accountNumber, transactionsByAccountId.size());
    }

    /**
     * Displays the list of transactions.
     *
     * @param transactions the transactions to display
     */
    private void displayTransactionList(List<Transaction> transactions) {
        transactions.stream()
                .forEach(Transaction::displayTransactionDetails);
    }

    /**
     * Displays transaction summary statistics for an account.
     *
     * @param accountNumber the account number
     * @param transactionCount the number of transactions
     */
    private void displayTransactionSummary(String accountNumber, int transactionCount) {
        double deposit = calculateDeposits(accountNumber);
        double withdrawal = calculateWithdrawal(accountNumber);
        double netChange = deposit - withdrawal;

        System.out.println("Total transactions: " + transactionCount);
        System.out.println("Total deposits: " + deposit);
        System.out.println("Total Withdrawals: " + withdrawal);
        System.out.println("Net change: " + netChange);
    }

    /**
     * Calculates the total deposit amount for a specific account.
     *
     * @param accountNumber the account number
     * @return the total deposit amount
     */
    public double calculateDeposits(String accountNumber) {
        return filterById(accountNumber).stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase("Deposit") 
                        || transaction.getType().equalsIgnoreCase("WIRE_TRANSFER_IN"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates the total withdrawal amount for a specific account.
     *
     * @param accountNumber the account number
     * @return the total withdrawal amount
     */
    public double calculateWithdrawal(String accountNumber) {
        return filterById(accountNumber).stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase("Withdrawal") 
                        || transaction.getType().equalsIgnoreCase("WIRE_TRANSFER_OUT"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the current number of transactions stored.
     *
     * @return the transaction count
     */
    public int getTransactionCount() {
        return transactions.size();
    }

    /**
     * Performs a wire transfer between two accounts.
     * Withdraws from source account and deposits to destination account.
     * Records transactions for both accounts.
     *
     * @param accountManager the account manager to access accounts
     * @param fromAccountNumber the source account number
     * @param toAccountNumber the destination account number
     * @param amount the amount to transfer
     * @return true if transfer was successful, false otherwise
     * @throws InvalidAccountException if either account is not found
     * @throws InvalidAmountException if the amount is invalid
     * @throws InsufficientFundsException if source account has insufficient funds
     * @throws OverdraftExceededException if withdrawal exceeds overdraft limit
     */
    public boolean wireTransfer(AccountManager accountManager, String fromAccountNumber, String toAccountNumber, double amount)
            throws InvalidAccountException, InvalidAmountException, 
                   InsufficientFundsException, OverdraftExceededException {
        
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidAmountException("Cannot transfer to the same account!");
        }
        
        Account fromAccount = accountManager.findAccount(fromAccountNumber);
        Account toAccount = accountManager.findAccount(toAccountNumber);
        
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero!");
        }

        boolean withdrawalSuccess = fromAccount.processTransaction(amount, "WITHDRAWAL");
        
        if (withdrawalSuccess) {
            boolean depositSuccess = toAccount.processTransaction(amount, "DEPOSIT");
            
            if (depositSuccess) {
                Transaction withdrawalTransaction = new Transaction(
                    fromAccountNumber, 
                    "WIRE_TRANSFER_OUT", 
                    amount, 
                    fromAccount.getBalance()
                );
                addTransaction(withdrawalTransaction);
                
                Transaction depositTransaction = new Transaction(
                    toAccountNumber, 
                    "WIRE_TRANSFER_IN", 
                    amount, 
                    toAccount.getBalance()
                );
                addTransaction(depositTransaction);
                
                return true;
            } else {
                fromAccount.processTransaction(amount, "DEPOSIT");
                throw new InvalidAmountException("Transfer failed during deposit. Transaction rolled back.");
            }
        }
        
        return false;
    }

}

