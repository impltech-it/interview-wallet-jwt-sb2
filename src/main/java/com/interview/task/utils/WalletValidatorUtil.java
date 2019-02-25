package com.interview.task.utils;

import com.interview.task.dto.WalletDto;
import com.interview.task.enums.Currency;
import com.interview.task.enums.Message;
import com.interview.task.exceptions.InvalidOrEmptyAmountException;
import com.interview.task.exceptions.LowBalanceException;
import com.interview.task.exceptions.WalletNotFoundException;
import com.interview.task.exceptions.WalletWithPointedCurrencyAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class represents wallet validating utility.
 */
@Component
public class WalletValidatorUtil {

    private static final Logger LOG = LoggerFactory.getLogger(WalletValidatorUtil.class);

    /**
     * Method checks whether the user has an ability to create new wallet.
     *
     * @param currencyToAdd currency for new wallet
     * @param userWallets existing user wallets
     */
    public static void checkAvailableCurrencyForCreation(
            final Currency currencyToAdd,
            final List<WalletDto> userWallets) {
        for (final WalletDto userWallet : userWallets) {
            if (userWallet.getCurrency().getTypeValue().equals(currencyToAdd.getTypeValue())) {
                LOG.error(Message.WALLET_WITH_CURRENCY_ALREADY_EXISTS.getMsgBody());
                throw new WalletWithPointedCurrencyAlreadyExistsException(
                        Message.WALLET_WITH_CURRENCY_ALREADY_EXISTS.getMsgBody());
            }
        };
    }

    /**
     * Method checks current wallet balance whether it has correct value to perform certain operation.
     *
     * @param amount money amount
     * @param currentBalance current balance
     */
    public static void checkCurrentBalance(final Double amount, final Double currentBalance) {
        if (currentBalance < amount) {
            LOG.error(Message.LOW_BALANCE.getMsgBody());
            throw new LowBalanceException(Message.LOW_BALANCE.getMsgBody());
        }
    }

    /**
     * Method performs amount validation.
     *
     * @param amount money amount
     * @throws InvalidOrEmptyAmountException can be thrown if money amount value is incorrect or empty
     */
    public static void amountChecker(final Double amount) throws InvalidOrEmptyAmountException {
        if (amount == null || amount < 1) {
            LOG.error(Message.EMPTY_AMOUNT.getMsgBody());
            throw new InvalidOrEmptyAmountException(Message.EMPTY_AMOUNT.getMsgBody());
        }
    }

    /**
     * Method checks wallet presence in database.
     *
     * @param isWalletPresent true if wallet is present false otherwise
     */
    public static void checkWalletPresence(boolean isWalletPresent) {
        if (!isWalletPresent) {
            LOG.error(Message.WALLET_NOT_FOUND.getMsgBody());
            throw new WalletNotFoundException(Message.WALLET_NOT_FOUND.getMsgBody());
        }
    }
}
