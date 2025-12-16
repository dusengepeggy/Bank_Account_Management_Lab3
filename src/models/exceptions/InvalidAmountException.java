package models.exceptions;

/**
 * Exception thrown when a transaction amount is invalid (negative or zero).
 */
public class InvalidAmountException extends Exception {
    private double amount;

    /**
     * Constructs a new InvalidAmountException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidAmountException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidAmountException with the invalid amount.
     *
     * @param amount the invalid amount
     */
    public InvalidAmountException(double amount) {
        super("Invalid amount: $" + amount + ". Amount must be greater than zero.");
        this.amount = amount;
    }

    /**
     * Returns the invalid amount.
     *
     * @return the invalid amount
     */
    public double getAmount() {
        return amount;
    }
}




