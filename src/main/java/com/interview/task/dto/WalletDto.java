package com.interview.task.dto;

import com.interview.task.enums.Currency;

/**
 * Class represents wallet dto.
 */
public class WalletDto {
    private Long walletId;
    private Currency currency;
    private Double balance;
    private boolean isMultiCurrency;

    public WalletDto() {
    }

    public WalletDto(Currency currency, Double balance, boolean isMultiCurrency) {
        this.currency = currency;
        this.balance = balance;
        this.isMultiCurrency = isMultiCurrency;
    }

    public WalletDto(Long walletId, Currency currency, Double balance, boolean isMultiCurrency) {
        this.walletId = walletId;
        this.currency = currency;
        this.balance = balance;
        this.isMultiCurrency = isMultiCurrency;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isMultiCurrency() {
        return isMultiCurrency;
    }

    public void setMultiCurrency(boolean multiCurrency) {
        isMultiCurrency = multiCurrency;
    }
}
