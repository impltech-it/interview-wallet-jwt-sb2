package com.interview.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception can be thrown if the user tries to transfer money with allowed multicurrent transfers but
 * choose the different wallet's currencies.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperationIsNotAllowed extends RuntimeException {
    public OperationIsNotAllowed(String message) {
        super(message);
    }
}
