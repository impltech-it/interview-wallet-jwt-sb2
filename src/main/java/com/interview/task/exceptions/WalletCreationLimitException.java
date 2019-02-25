package com.interview.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception can be thrown in case when user tries to create more then 3 wallets.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletCreationLimitException extends RuntimeException {
    public WalletCreationLimitException(final String message) {
        super(message);
    }
}
