package models.exceptions;

/**
 * Exception thrown when an account number is invalid or account is not found.
 */
public class InvalidAccountException extends Exception {
    private String accountNumber;

    /**
     * Constructs a new InvalidAccountException with the account number.
     *
     * @param accountNumber the invalid account number
     */
    public InvalidAccountException(String accountNumber) {
        super("Invalid account: Account number '" + accountNumber + "' not found.");
        this.accountNumber = accountNumber;
    }

    /**
     * Returns the account number.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
}




