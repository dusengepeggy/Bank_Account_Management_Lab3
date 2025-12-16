package models;

import java.time.LocalDate;

public class Transaction {
    static int transactionCounter;
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

    public void displayTransactionDetails(){
        System.out.println(transactionId + "  |  " + timestamp + "  |  " + type + "  |  " + amount+ "  |  " + balanceAfter );

    }
}
