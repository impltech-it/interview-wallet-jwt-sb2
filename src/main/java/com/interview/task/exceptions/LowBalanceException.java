package com.interview.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception can be thrown in case the wallet balance is low and transfer operation can't be performed.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LowBalanceException extends RuntimeException {
    public LowBalanceException(final String message) {
        super(message);
    }
}
