package services;

import models.Account;
import models.Transaction;
import models.exceptions.InvalidAccountException;

public class StatementGenerator {
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;

    /**
     * Constructs a StatementGenerator with the specified managers.
     *
     * @param accountManager the account manager to retrieve account information
     * @param transactionManager the transaction manager to retrieve transaction history
     */
    public StatementGenerator(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    /**
     * Generates and displays a complete account statement for the specified account.
     * The statement includes account details, transaction history, and summary statistics.
     *
     * @param accountNumber the account number to generate the statement for
     * @throws InvalidAccountException if the account is not found
     */
    public void generateAccountStatement(String accountNumber) throws InvalidAccountException {
        Account account = accountManager.findAccount(accountNumber);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("ACCOUNT STATEMENT");
        System.out.println("=".repeat(70));
        System.out.println();

        System.out.println("ACCOUNT INFORMATION");
        System.out.println("-".repeat(70));
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Status: " + account.getStatus());
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println();

        System.out.println("CUSTOMER INFORMATION");
        System.out.println("-".repeat(70));
        System.out.println("Name: " + account.getCustomer().getName());
        System.out.println("Age: " + account.getCustomer().getAge());
        System.out.println("Contact: " + account.getCustomer().getContact());
        System.out.println("Address: " + account.getCustomer().getAddress());
        System.out.println("Customer Type: " + account.getCustomer().getClass().getSimpleName());
        System.out.println();

        System.out.println("TRANSACTION HISTORY");
        System.out.println("-".repeat(70));
        java.util.List<Transaction> transactions = transactionManager.filterById(accountNumber);
        transactionManager.viewTransactionsByAccounts(accountNumber);
        System.out.println();
        
        // Summary Statistics Section
        System.out.println("SUMMARY STATISTICS");
        System.out.println("-".repeat(70));
        double totalDeposits = transactionManager.calculateDeposits(accountNumber);
        double totalWithdrawals = transactionManager.calculateWithdrawal(accountNumber);
        double netChange = totalDeposits - totalWithdrawals;
        
        System.out.println("Total Transactions: " + transactions.size());
        System.out.println("Total Deposits: $" + String.format("%.2f", totalDeposits));
        System.out.println("Total Withdrawals: $" + String.format("%.2f", totalWithdrawals));
        System.out.println("Net Change: $" + String.format("%.2f", netChange));
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("END OF STATEMENT");
        System.out.println("=".repeat(70));
    }
}

