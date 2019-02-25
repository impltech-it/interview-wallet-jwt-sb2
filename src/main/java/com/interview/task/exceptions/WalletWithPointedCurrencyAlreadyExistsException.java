package com.interview.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User can have only 3 wallets with different currencies. Exception can be thrown when user tries to create
 * a wallet with existed currency.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletWithPointedCurrencyAlreadyExistsException extends RuntimeException {
    public WalletWithPointedCurrencyAlreadyExistsException(final String message) {
        super(message);
    }
}
