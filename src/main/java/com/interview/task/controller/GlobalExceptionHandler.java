package com.interview.task.controller;

import com.interview.task.dto.ApiErrorResponse;
import com.interview.task.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Method handle InvalidOrEmptyAmountException.
     *
     * @param ex InvalidOrEmptyAmountException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(InvalidOrEmptyAmountException.class)
    public ApiErrorResponse handleInvalidOrEmptyResponseException(final InvalidOrEmptyAmountException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle LowBalanceException.
     *
     * @param ex LowBalanceException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(LowBalanceException.class)
    public ApiErrorResponse handleLowBalanceException(final LowBalanceException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle UserAlreadyExistsException.
     *
     * @param ex UserAlreadyExistsException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiErrorResponse handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle UserNotFoundException.
     *
     * @param ex UserNotFoundException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ApiErrorResponse handleUserNotFoundException(final UserNotFoundException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle WalletCreationLimitException.
     *
     * @param ex WalletCreationLimitException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(WalletCreationLimitException.class)
    public ApiErrorResponse handleWalletCreationLimitException(final WalletCreationLimitException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle WalletWithPointedCurrencyAlreadyExistsException.
     *
     * @param ex WalletWithPointedCurrencyAlreadyExistsException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(WalletWithPointedCurrencyAlreadyExistsException.class)
    public ApiErrorResponse handleWalletWithPointedCurrencyAlreadyExistsException(final WalletWithPointedCurrencyAlreadyExistsException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle WalletNotFoundException.
     *
     * @param ex WalletNotFoundException
     * @return ApiErrorResponse
     */
    @ExceptionHandler(WalletNotFoundException.class)
    public ApiErrorResponse handleWalletNotFoundException(final WalletNotFoundException ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method handle OperationIsNotAllowed.
     *
     * @param ex OperationIsNotAllowed
     * @return ApiErrorResponse
     */
    @ExceptionHandler(OperationIsNotAllowed.class)
    public ApiErrorResponse handleOperationIsNotAllowed(final OperationIsNotAllowed ex) {
        return getApiErrorResponse(ex.getCause(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Method performs exception handling.
     *
     * @param throwable thrown exception
     * @param simpleName exception name
     * @param message exception message
     * @return ApiErrorResponse
     */
    private ApiErrorResponse getApiErrorResponse(
            final Throwable throwable,
            final String simpleName,
            final String message) {
        final String cause = String.valueOf(throwable);
        LOG.error(cause);
        return new ApiErrorResponse(simpleName, cause, message);
    }
}
