package models;

import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAmountException;

public class SavingsAccount extends Account {
    private double interestRate ;
    private double minimumBalance;

    public SavingsAccount(Customer customer, double balance, String status) {
        super( customer, balance, status);
        this.interestRate = 3.5/100;
        this.minimumBalance = 500;
    }
    @Override
    public String getAccountType() {
        return "Savings";

    }
    @Override
    public void displayAccountDetail() {
        System.out.println("Account details");
        System.out.println("____________________");
        System.out.println("Account number: " + getAccountNumber());
        System.out.println("Account holder name: " + getCustomer().getName() + 1);
        System.out.println("Account status: " + getStatus());
        System.out.println("Account Type: " + getAccountType());
        System.out.println("Account Balance: " + getBalance() );
        System.out.println("Interest rate: "+ interestRate);
        System.out.println("Minimum balance: "+ minimumBalance);
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        double newBalance = this.getBalance() - amount;
        if (newBalance < minimumBalance) {
            throw new InsufficientFundsException(getBalance(), amount, "The minimum balance should be $500");
        }
        setBalance(newBalance);
    }
    double calculateInterest(){
        double balance = getBalance();
        return balance*interestRate;
    }
}
