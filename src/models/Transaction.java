package models;

import java.time.LocalDate;

public class Transaction {
    static int transactionCounter = 1;
    private String transactionId,accountNumber,type,timestamp;
    private double amount, balanceAfter;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transactionId="TXN"+ String.format("%03d",transactionCounter);
        this.timestamp=String.valueOf(LocalDate.now());
        transactionCounter++;
    }

    public static int getTransactionCounter() {
        return transactionCounter;
    }

    public static void setTransactionCounter(int counter) {
        transactionCounter = counter;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public void displayTransactionDetails(){
        System.out.println(transactionId + "  |  " + timestamp + "  |  " + type + "  |  " + amount+ "  |  " + balanceAfter );

    }
}
