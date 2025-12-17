import com.bank.tests.ExceptionTest;
import com.bank.tests.TransactionManagerTest;
import models.*;
import models.exceptions.*;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.*;
import services.AccountManager;
import services.StatementGenerator;
import services.TransactionManager;
import utils.ValidationUtils;
import java.io.IOException;
import java.util.Scanner;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import com.bank.tests.AccountTest;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
public class Main {
    private static final AccountManager accountManager = new AccountManager();
    private static final TransactionManager transactionManager = new TransactionManager();
    private static final StatementGenerator statementGenerator = new StatementGenerator(accountManager, transactionManager);
    private static final Scanner sc = new Scanner(System.in);
    private static final ValidationUtils validation = new ValidationUtils(sc);
    //...........................Sample customer data........................................
    private static void initializeSampleData() {
        Customer c1 = new RegularCustomer("John Smith", 35, "+1-555-0001", "123 Main St");
        Account a1 = new SavingsAccount(c1, 5250.00,"Active");
        accountManager.addAccount(a1);

        Customer c2 = new RegularCustomer("Sarah Johnson", 28, "+1-555-0002", "456 Oak Ave");
        Account a2 = new CheckingAccount(c2, 3450.00,"Active");
        accountManager.addAccount(a2);

        Customer c3 = new PremiumCustomer("Michael Chen", 42, "+1-555-0003", "789 Pine Rd");
        Account a3 = new SavingsAccount(c3, 15750.00,"Active");
        accountManager.addAccount(a3);

        Customer c4 = new RegularCustomer("Emily Brown", 31, "+1-555-0004", "321 Elm St");
        Account a4 = new CheckingAccount(c4, 890.00,"Active");
        accountManager.addAccount(a4);

        Customer c5 = new PremiumCustomer("David Wilson", 55, "+1-555-0005", "654 Maple Dr");
        Account a5 = new SavingsAccount(c5, 25300.00,"Active");
        accountManager.addAccount(a5);
    }

    //...........................main method..................
    public static void main (String[] args) throws InsufficientFundsException, OverdraftExceededException {

        try {
            accountManager.loadAccountsFromFile();           
            transactionManager.loadTransactionsFromFile();
            
            System.out.println("Data loaded from files successfully.");

        } catch (IOException e) {
            System.out.println("Error loading data from files: " + e.getMessage());
        }
        
        if (accountManager.getAccountCount() == 0) {
            System.out.println("No accounts found in file. Initializing sample data...");
            initializeSampleData();
        }

        while (true) {

            System.out.println("=======================================\n  BANK ACCOUNT MANAGEMENT - MAIN MENU \n=======================================");
            System.out.println(" \t1. Manage Account \n \t2. Perform Transaction \n \t3. Generate Account Statement \n \t4. Run Tests \n\t5. Exit");
            System.out.print("\nEnter choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter a number.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    manageAccount();
                    break;
                case 2:
                    performTransaction();
                    break;
                case 3:
                    generateAccountStatement();
                    break;
                case 4:
                    runTests();
                    break;
                case 5:
                    System.out.println("\nThank you for using Bank Account Management System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please choose a number between 1-5.");
                    pressEnterToContinue();
                    continue;
            }
        }

    }


    //........reusable press enter to continue.........................
    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }


    //......................menu actions................................
    
    private static void manageAccount() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MANAGE ACCOUNT");
        System.out.println("=".repeat(50));
        System.out.println("\n \t1. Create Account \n \t2. View All Accounts \n \t3. Back to Main Menu");
        System.out.print("\nEnter choice: ");
        
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input! Enter a number.");
            sc.next();
            sc.nextLine();
            pressEnterToContinue();
            return;
        }
        
        int choice = sc.nextInt();
        sc.nextLine();
        
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                viewAccounts();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice! Please choose a number between 1-3.");
                pressEnterToContinue();
                break;
        }
    }
    
    private static void createAccount() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ACCOUNT CREATION");
        System.out.println("=".repeat(50));
        System.out.println();
        String name =   validation.readNonEmptyString("Enter customer name: ");
        int age = validation.readInt("Enter customer age: ", 1, 120);
        String contact = validation.readNonEmptyString("Enter customer contact: ");
        String address = validation.readNonEmptyString("Enter customer address: ");

        System.out.println("\nCustomer type:");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000)");
        int customerType = validation.readInt("\nSelect type (1-Regular, 2-Premium): ", 1, 2);

        Customer customer;
        if (customerType == 1) {
            customer = new RegularCustomer(name, age, contact, address);
        } else {
            customer = new PremiumCustomer(name, age, contact, address);
        }

        System.out.println("\nAccount type:");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        int accountType = validation.readInt("\nSelect account type (1-Savings, 2-Checking): ", 1, 2);

        double minDeposit = (customerType==1) ? 500 : (customerType == 2 && accountType == 1) ? 10000 : 0;
        double initialDeposit = validation.readDouble("Enter initial deposit amount: $", minDeposit);

        Account account;
        if (accountType == 1) {
            account = new SavingsAccount(customer, initialDeposit,"Active");
        } else {
            account = new CheckingAccount(customer, initialDeposit, "Active");
        }

        accountManager.addAccount(account);
        account.displayAccountDetail();
        System.out.println("\n✓ Account created successfully!");
        pressEnterToContinue();
    }

    private static void viewAccounts() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ACCOUNT LISTING");
        System.out.println("=".repeat(50));
        System.out.println();

        accountManager.viewAllAccounts();

        System.out.println("\nTotal Accounts: " + accountManager.getAccountCount());
        System.out.println("Total Bank Balance: $" + accountManager.getTotalBalance());

        pressEnterToContinue();
    }

    private static void performTransaction() throws InsufficientFundsException, OverdraftExceededException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("PERFORM TRANSACTION");
        System.out.println("=".repeat(50));
        System.out.println("\n \t1. Deposit \n \t2. Withdrawal \n \t3. Wire Money Between Accounts \n \t4. Back to Main Menu");
        System.out.print("\nEnter choice: ");
        
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input! Enter a number.");
            sc.next();
            sc.nextLine();
            pressEnterToContinue();
            return;
        }
        
        int choice = sc.nextInt();
        sc.nextLine();
        
        switch (choice) {
            case 1:
                processDeposit();
                break;
            case 2:
                processWithdrawal();
                break;
            case 3:
                processWireTransfer();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice! Please choose a number between 1-4.");
                pressEnterToContinue();
                break;
        }
    }
    
    private static void processDeposit() throws InsufficientFundsException, OverdraftExceededException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEPOSIT TRANSACTION");
        System.out.println("=".repeat(50));
        System.out.println();

        try {
            String accountNumber = validation.readNonEmptyString("Enter Account Number: ");
            Account account = accountManager.findAccount(accountNumber);

            System.out.println("\nAccount Details:");
            System.out.println("Customer: " + account.getCustomer().getName());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: $" + account.getBalance());

            double amount = validation.readDouble("Enter amount to deposit: $", 0);
            double previousBalance = account.getBalance();

            // Show confirmation
            System.out.println("\n" + "-".repeat(50));
            System.out.println("TRANSACTION CONFIRMATION");
            System.out.println("-".repeat(50));
            System.out.println("Account: " + accountNumber);
            System.out.println("Type: DEPOSIT");
            System.out.println("Amount: $" + amount);
            System.out.println("Current Balance: $" + previousBalance);

            System.out.print("\nConfirm transaction? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                try {
                    boolean success = account.processTransaction(amount, "DEPOSIT");
                    if (success) {
                        double newBalance = account.getBalance();
                        Transaction transaction = new Transaction(accountNumber, "DEPOSIT", amount, newBalance);
                        transactionManager.addTransaction(transaction);
                        System.out.println("\n✓ Transaction completed successfully!");
                        System.out.println("New Balance: $" + newBalance);
                    } else {
                        System.out.println("\n✗ Transaction failed. Please try again.");
                    }
                } catch (InvalidAmountException e) {
                    System.out.println("\n✗ Error: " + e.getMessage());
                }
            } else {
                System.out.println("\n✗ Transaction cancelled.");
            }
        } catch (InvalidAccountException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }

        pressEnterToContinue();
    }
    
    private static void processWithdrawal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("WITHDRAWAL TRANSACTION");
        System.out.println("=".repeat(50));
        System.out.println();

        try {
            String accountNumber = validation.readNonEmptyString("Enter Account Number: ");
            Account account = accountManager.findAccount(accountNumber);

            System.out.println("\nAccount Details:");
            System.out.println("Customer: " + account.getCustomer().getName());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: $" + account.getBalance());

            double amount = validation.readDouble("Enter amount to withdraw: $", 0);
            double previousBalance = account.getBalance();

            // Show confirmation
            System.out.println("\n" + "-".repeat(50));
            System.out.println("TRANSACTION CONFIRMATION");
            System.out.println("-".repeat(50));
            System.out.println("Account: " + accountNumber);
            System.out.println("Type: WITHDRAWAL");
            System.out.println("Amount: $" + amount);
            System.out.println("Current Balance: $" + previousBalance);

            System.out.print("\nConfirm transaction? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                try {
                    boolean success = account.processTransaction(amount, "WITHDRAWAL");
                    if (success) {
                        double newBalance = account.getBalance();
                        Transaction transaction = new Transaction(accountNumber, "WITHDRAWAL", amount, newBalance);
                        transactionManager.addTransaction(transaction);
                        System.out.println("\n✓ Transaction completed successfully!");
                        System.out.println("New Balance: $" + newBalance);
                    } else {
                        System.out.println("\n✗ Transaction failed. Please try again.");
                    }
                } catch (InvalidAmountException | InsufficientFundsException | OverdraftExceededException e) {
                    System.out.println("\n✗ Error: " + e.getMessage());
                }
            } else {
                System.out.println("\n✗ Transaction cancelled.");
            }
        } catch (InvalidAccountException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }

        pressEnterToContinue();
    }
    
    private static void processWireTransfer() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("WIRE TRANSFER BETWEEN ACCOUNTS");
        System.out.println("=".repeat(50));
        System.out.println();

        try {
            String fromAccountNumber = validation.readNonEmptyString("Enter Source Account Number: ");
            Account fromAccount = accountManager.findAccount(fromAccountNumber);

            System.out.println("\nSource Account Details:");
            System.out.println("Customer: " + fromAccount.getCustomer().getName());
            System.out.println("Account Type: " + fromAccount.getAccountType());
            System.out.println("Current Balance: $" + fromAccount.getBalance());

            String toAccountNumber = validation.readNonEmptyString("\nEnter Destination Account Number: ");
            Account toAccount = accountManager.findAccount(toAccountNumber);

            System.out.println("\nDestination Account Details:");
            System.out.println("Customer: " + toAccount.getCustomer().getName());
            System.out.println("Account Type: " + toAccount.getAccountType());
            System.out.println("Current Balance: $" + toAccount.getBalance());

            double amount = validation.readDouble("\nEnter amount to transfer: $", 0);
            double fromPreviousBalance = fromAccount.getBalance();
            double toPreviousBalance = toAccount.getBalance();

            // Show confirmation
            System.out.println("\n" + "-".repeat(50));
            System.out.println("WIRE TRANSFER CONFIRMATION");
            System.out.println("-".repeat(50));
            System.out.println("From Account: " + fromAccountNumber + " - " + fromAccount.getCustomer().getName());
            System.out.println("To Account: " + toAccountNumber + " - " + toAccount.getCustomer().getName());
            System.out.println("Amount: $" + amount);
            System.out.println("Source Balance Before: $" + fromPreviousBalance);
            System.out.println("Destination Balance Before: $" + toPreviousBalance);

            System.out.print("\nConfirm wire transfer? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                try {
                    boolean success = transactionManager.wireTransfer(accountManager, fromAccountNumber, toAccountNumber, amount);
                    if (success) {
                        System.out.println("\n✓ Wire transfer completed successfully!");
                        System.out.println("Source Account New Balance: $" + fromAccount.getBalance());
                        System.out.println("Destination Account New Balance: $" + toAccount.getBalance());
                    } else {
                        System.out.println("\n✗ Wire transfer failed. Please try again.");
                    }
                } catch (InvalidAmountException | InsufficientFundsException | OverdraftExceededException e) {
                    System.out.println("\n✗ Error: " + e.getMessage());
                }
            } else {
                System.out.println("\n✗ Wire transfer cancelled.");
            }
        } catch (InvalidAccountException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }

        pressEnterToContinue();
    }
    
    private static void generateAccountStatement() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("GENERATE ACCOUNT STATEMENT");
        System.out.println("=".repeat(50));
        System.out.println();

        try {
            String accountNumber = validation.readNonEmptyString("Enter Account Number: ");
            statementGenerator.generateAccountStatement(accountNumber);
        } catch (InvalidAccountException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }

        pressEnterToContinue();
    }
    
    private static void runTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RUNNING TESTS");
        System.out.println("=".repeat(50));
        System.out.println("\nExecuting all test suites via command line...\n");
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request().selectors(
                selectClass(AccountTest.class),
                selectClass(ExceptionTest.class),
                selectClass(TransactionManagerTest.class)
        ).build();
        Launcher launcher = LauncherFactory.create();
        TestExecutionListener listener = new TestExecutionListener() {

            @Override
            public void executionStarted(TestIdentifier testIdentifier) {
                if (testIdentifier.isTest()) {
                    System.out.print("▶ Running: " + testIdentifier.getDisplayName() + "....................");
                }
            }

            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.isTest()) {
                    System.out.println("(" + testExecutionResult.getStatus()+")");
                }
            }
        };
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        System.out.println("\n" + "=".repeat(50));
        pressEnterToContinue();
    }

}
