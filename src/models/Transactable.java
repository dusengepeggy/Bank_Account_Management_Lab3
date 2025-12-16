package models;

import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;

public interface Transactable {
    boolean processTransaction (double amount, String type) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException;
}
