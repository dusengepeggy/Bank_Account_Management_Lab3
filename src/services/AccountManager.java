package services;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.exceptions.InvalidAccountException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountManager {
   private final Map<String, Account> accounts = new HashMap<>();

   public void addAccount (Account newAccount)  {
       accounts.put(newAccount.getAccountNumber().toUpperCase(), newAccount);
       try {
           saveAccountsToFile();
       } catch (IOException e) {
           System.out.println("Error saving account to file: "+ e.getMessage());
       }

   }
   /**
    * Finds an account by account number.
    *
    * @param accountNumber the account number to search for
    * @return the account if found
    * @throws InvalidAccountException if the account is not found
    */
   public Account findAccount(String accountNumber) throws InvalidAccountException {
       if (accountNumber == null) {
           throw new InvalidAccountException(accountNumber);
       }
       Account account = accounts.get(accountNumber.toUpperCase());
       if (account == null) {
           throw new InvalidAccountException(accountNumber);
       }
       return account;
   }

   public void viewAllAccounts (){
       accounts.values().stream()
               .forEach(Account::displayAccountDetail);
   }

   public double getTotalBalance () {
       return accounts.values().stream()
               .mapToDouble(Account::getBalance)
               .sum();
   }

   public int getAccountCount(){
       return accounts.size();
   }

   public void saveAccountsToFile() throws IOException {
       Path dataDir = Paths.get("src", "data");

       if (!Files.exists(dataDir)) {
           Files.createDirectories(dataDir);
       }
       
       Path accountsFile = dataDir.resolve("accounts.txt");

       String content = accounts.values().stream()
               .map(this::formatAccountForFile)
               .collect(Collectors.joining(System.lineSeparator()));

       Files.writeString(accountsFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
   }

   private String formatAccountForFile(Account account) {
       Customer customer = account.getCustomer();
       return String.join("|",
               account.getAccountNumber(),
               account.getAccountType(),
               account.getStatus(),
               String.valueOf(account.getBalance()),
               customer.getName(),
               String.valueOf(customer.getAge()),
               customer.getEmail(),
               customer.getContact(),
               customer.getAddress(),
               customer.getCustomerType()
       );
   }

   public void loadAccountsFromFile() throws IOException {
       Path accountsFile = Paths.get("src", "data", "accounts.txt");
       
       if (!Files.exists(accountsFile)) {
           return;
       }
       
       try (Stream<String> lines = Files.lines(accountsFile)) {
           lines.filter(line -> !line.trim().isEmpty())
                   .map(this::parseAccountFromLine)
                   .forEach(account -> accounts.put(account.getAccountNumber().toUpperCase(), account));
       }
   }


   private Account parseAccountFromLine(String line) {
       String[] parts = line.split("\\|");
       if (parts.length == 9) {
           String accountNumber = parts[0];
           String accountType = parts[1];
           String status = parts[2];
           double balance = Double.parseDouble(parts[3]);
           String customerName = parts[4];
           int customerAge = Integer.parseInt(parts[5]);
           String customerContact = parts[6];
           String customerAddress = parts[7];
           String customerType = parts[8];
           Customer customer = createCustomer(customerName, customerAge, "unknown@example.com", customerContact, customerAddress, customerType);
           Account account = createAccount(accountType, customer, balance, status);
           account.setAccountNumber(accountNumber);
           return account;
       } else if (parts.length == 10) {
           String accountNumber = parts[0];
           String accountType = parts[1];
           String status = parts[2];
           double balance = Double.parseDouble(parts[3]);
           String customerName = parts[4];
           int customerAge = Integer.parseInt(parts[5]);
           String customerEmail = parts[6];
           String customerContact = parts[7];
           String customerAddress = parts[8];
           String customerType = parts[9];
           Customer customer = createCustomer(customerName, customerAge, customerEmail, customerContact, customerAddress, customerType);
           Account account = createAccount(accountType, customer, balance, status);
           account.setAccountNumber(accountNumber);
           return account;
       } else {
           throw new IllegalArgumentException("Invalid account line format: " + line);
       }
   }

   private Customer createCustomer(String name, int age, String email, String contact, String address, String customerType) {
       if ("Premium".equalsIgnoreCase(customerType)) {
           return new PremiumCustomer(name, age, email, contact, address);
       } else {
           return new RegularCustomer(name, age, email, contact, address);
       }
   }

   /**
    * Creates an Account object based on the account type.
    *
    * @param accountType the account type (Savings or Checking)
    * @param customer the customer for the account
    * @param balance the account balance
    * @param status the account status
    * @return the created Account object
    */
   private Account createAccount(String accountType, Customer customer, double balance, String status) {
       if ("Savings".equalsIgnoreCase(accountType)) {
           return new SavingsAccount(customer, balance, status);
       } else {
           return new CheckingAccount(customer, balance, status);
       }
   }

}
