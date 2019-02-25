package com.interview.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception can be thrown in case when transfer money amount is null or < 1.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOrEmptyAmountException extends RuntimeException {
    public InvalidOrEmptyAmountException(final String message) {
        super(message);
    }
}
