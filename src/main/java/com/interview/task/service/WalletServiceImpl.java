package com.interview.task.service;

import com.interview.task.converter.Converter;
import com.interview.task.dto.WalletDto;
import com.interview.task.entity.Wallet;
import com.interview.task.enums.Message;
import com.interview.task.exceptions.InvalidOrEmptyAmountException;
import com.interview.task.exceptions.LowBalanceException;
import com.interview.task.exceptions.OperationIsNotAllowed;
import com.interview.task.exceptions.WalletNotFoundException;
import com.interview.task.mapper.WalletMapper;
import com.interview.task.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.interview.task.utils.WalletValidatorUtil.*;

/**
 * Class represents WalletService implementation.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final Converter converter;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletServiceImpl(
            final WalletRepository walletRepository,
            final Converter converter,
            final WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.converter = converter;
        this.walletMapper = walletMapper;
    }

    /**
     * Method preforms replenish balance operation between two wallets.
     *
     * @param clientWalletFrom wallet id which balance will be reduced
     * @param clientWalletTo   wallet id which balance will be replenished
     * @param amount           money amount
     * @return true if operation successful, false otherwise
     */
    @Transactional
    @Override
    public boolean replenishBalance(
            final Long clientWalletFrom,
            final Long clientWalletTo,
            final Double amount) {
        if (!walletRepository.existsById(clientWalletFrom) || !walletRepository.existsById(clientWalletTo)) {
            LOG.error(Message.WALLET_NOT_FOUND.getMsgBody());
            throw new WalletNotFoundException(Message.WALLET_NOT_FOUND.getMsgBody());
        }

        amountChecker(amount);

        final Wallet from = walletRepository.getOne(clientWalletFrom);
        final Wallet to = walletRepository.getOne(clientWalletTo);

        final String currencyFrom = from.getCurrency().getTypeValue();
        final String currencyTo = to.getCurrency().getTypeValue();

        boolean isMultiCurrencyTransfer = !currencyFrom.equals(currencyTo);
        if (isMultiCurrencyTransfer && !from.isMultiCurrency()) {
            LOG.error(Message.OPERATION_IS_NOT_ALLOWED.getMsgBody());
            throw new OperationIsNotAllowed(Message.OPERATION_IS_NOT_ALLOWED.getMsgBody());
        }

        if (isMultiCurrencyTransfer) {
            Double convertedSum = converter.convert(amount, from.getCurrency(), to.getCurrency());
            reduceBalanceInternal(amount, from);
            addBalanceInternal(convertedSum, to);
        } else {
            reduceBalanceInternal(amount, from);
            addBalanceInternal(amount, to);
        }
        return true;
    }

    /**
     * Method performs balance increasing operation for certain wallet.
     *
     * @param clientWalletId wallet id which balance will increase
     * @param amount         money amount
     * @throws InvalidOrEmptyAmountException can be thrown if money amount value is incorrect or empty
     */
    @Transactional
    @Override
    public void addBalance(final Long clientWalletId, final Double amount) throws InvalidOrEmptyAmountException {
        final Wallet clientWallet = walletRepository.getOne(clientWalletId);
        amountChecker(amount);
        addBalanceInternal(amount, clientWallet);
    }

    /**
     * Method performs increase balance processing.
     *
     * @param amount       money amount
     * @param clientWallet wallet  which balance will increase
     */
    private void addBalanceInternal(final Double amount, final Wallet clientWallet) {
        final Double currentBalance = clientWallet.getBalance() + amount;
        clientWallet.setBalance(currentBalance);
        walletRepository.save(clientWallet);
    }

    /**
     * Method performs balance decreasing operation for certain wallet.
     *
     * @param clientWalletId wallet id which balance will will decrease
     * @param amount         money amount
     * @throws InvalidOrEmptyAmountException can be thrown if money amount value is incorrect or empty
     * @throws LowBalanceException           can be thrown if balance is low
     */
    @Transactional
    @Override
    public void reduceBalance(final Long clientWalletId, final Double amount) throws InvalidOrEmptyAmountException, LowBalanceException {
        final Wallet clientWallet = walletRepository.getOne(clientWalletId);
        amountChecker(amount);
        reduceBalanceInternal(amount, clientWallet);
    }

    /**
     * Method performs balance decrease processing.
     *
     * @param amount       money amount
     * @param clientWallet client wallet which balance will decrease
     */
    private void reduceBalanceInternal(final Double amount, final Wallet clientWallet) {
        Double currentBalance = clientWallet.getBalance();
        checkCurrentBalance(amount, currentBalance);
        currentBalance -= amount;
        clientWallet.setBalance(currentBalance);
        walletRepository.save(clientWallet);
    }

    /**
     * Method returns wallet by id.
     *
     * @param walletId wallet id
     * @return WalletDto
     */
    @Override
    public WalletDto getWallet(final Long walletId) {
        checkWalletPresence(walletRepository.existsById(walletId));
        final Wallet wallet = walletRepository.getOne(walletId);
        return walletMapper.toDto(wallet);
    }

    /**
     * Method removes wallet by id.
     *
     * @param walletId wallet id
     */
    @Override
    public void removeWallet(final Long walletId) {
        checkWalletPresence(walletRepository.existsById(walletId));
        walletRepository.deleteById(walletId);
    }

}
