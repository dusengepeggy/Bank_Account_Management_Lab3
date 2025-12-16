package models.exceptions;

/**
 * Exception thrown when there are insufficient funds for a withdrawal.
 */
public class InsufficientFundsException extends Exception {
    private double balance;
    private double requestedAmount;

    /**
     * Constructs a new InsufficientFundsException with the specified detail message.
     *
     * @param message the detail message
     */
    public InsufficientFundsException(String message) {
        super(message);
    }

    /**
     * Constructs a new InsufficientFundsException with balance and requested amount.
     *
     * @param balance the current account balance
     * @param requestedAmount the amount requested for withdrawal
     */
    public InsufficientFundsException(double balance, double requestedAmount,String message) {
        super("Insufficient funds: Current balance is $" + balance
                + ", but withdrawal of $" + requestedAmount + " was requested. "  + message);
        this.balance = balance;
        this.requestedAmount = requestedAmount;
    }

    /**
     * Returns the current balance.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the requested withdrawal amount.
     *
     * @return the requested amount
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }
}




