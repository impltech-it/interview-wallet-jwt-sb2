package com.interview.task.service;

import com.interview.task.dto.WalletDto;
import com.interview.task.exceptions.InvalidOrEmptyAmountException;
import com.interview.task.exceptions.LowBalanceException;

/**
 * Interface represents wallet service methods.
 */
public interface WalletService {
     boolean replenishBalance(Long clientWalletFrom, Long clientWalletTo, Double amount);
     void addBalance(Long walletId, Double amount) throws InvalidOrEmptyAmountException;
     void reduceBalance(Long walletId, Double amount) throws InvalidOrEmptyAmountException, LowBalanceException;
     WalletDto getWallet(Long walletId);
     void removeWallet(Long walletId);
}
