package models.exceptions;

/**
 * Exception thrown when a withdrawal exceeds the overdraft limit for a checking account.
 */
public class  OverdraftExceededException extends Exception {
    private double currentBalance;
    private double requestedAmount;
    private double overdraftLimit;

    /**
     * Constructs a new OverdraftExceededException with the specified detail message.
     *
     * @param message the detail message
     */
    public OverdraftExceededException(String message) {
        super(message);
    }

    /**
     * Constructs a new OverdraftExceededException with account details.
     *
     * @param currentBalance the current account balance
     * @param requestedAmount the amount requested for withdrawal
     * @param overdraftLimit the overdraft limit
     */
    public OverdraftExceededException(double currentBalance, double requestedAmount, double overdraftLimit) {
        super("Overdraft limit exceeded: Current balance is $" + currentBalance
                + ", overdraft limit is $" + overdraftLimit
                + ", but withdrawal of $" + requestedAmount + " would exceed the limit.");
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Returns the current account balance.
     *
     * @return the current balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Returns the requested withdrawal amount.
     *
     * @return the requested amount
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Returns the overdraft limit.
     *
     * @return the overdraft limit
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}




