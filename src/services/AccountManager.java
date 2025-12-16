package services;

import models.Account;
import models.exceptions.InvalidAccountException;

public class AccountManager {
   private Account[] accounts = new Account[50];
   private int accountCount;
   public void addAccount (Account newAccount){
       accounts[accountCount]=newAccount;
       accountCount++;
   }
   /**
    * Finds an account by account number.
    *
    * @param accountNumber the account number to search for
    * @return the account if found
    * @throws InvalidAccountException if the account is not found
    */
   public Account findAccount(String accountNumber) throws InvalidAccountException {
       for (int i = 0; i < accountCount; i++) {
           if (accounts[i].getAccountNumber().equalsIgnoreCase(accountNumber)) {
               return accounts[i];
           }
       }
       throw new InvalidAccountException(accountNumber);
   }

   public void viewAllAccounts (){
       for (int i=0 ; i<accountCount;i++){
           accounts[i].displayAccountDetail();
       }
   }

   public double getTotalBalance () {
       double sum = 0;
       for (int i=0 ; i<accountCount;i++){
           sum += accounts[i].getBalance();
       }
       return sum;
   }

   public int getAccountCount(){
       return accountCount;
   }

}
