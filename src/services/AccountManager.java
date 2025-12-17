package services;

import models.Account;
import models.exceptions.InvalidAccountException;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
   private final Map<String, Account> accounts = new HashMap<>();

   public void addAccount (Account newAccount){
       accounts.put(newAccount.getAccountNumber().toUpperCase(), newAccount);
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

}
