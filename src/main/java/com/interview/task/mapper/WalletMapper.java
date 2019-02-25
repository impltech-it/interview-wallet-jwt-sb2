package com.interview.task.mapper;

import com.interview.task.dto.WalletDto;
import com.interview.task.entity.Wallet;
import org.springframework.stereotype.Component;

/**
 * Class represents wallet mapper.
 */
@Component
public class WalletMapper {

    /**
     * Method transform walletDto into wallet entity.
     *
     * @param walletDto walletDto
     * @return User
     */
    public Wallet toEntity(final WalletDto walletDto) {
        final Wallet wallet = new Wallet();
        wallet.setWalletId(walletDto.getWalletId());
        wallet.setCurrency(walletDto.getCurrency());
        wallet.setBalance(walletDto.getBalance());
        wallet.setMultiCurrency(walletDto.isMultiCurrency());
        return wallet;
    }

    /**
     * Method transform wallet entity into walletDto.
     *
     * @param wallet wallet
     * @return WalletDto
     */
    public WalletDto toDto(final Wallet wallet) {
        final WalletDto walletDto = new WalletDto();
        walletDto.setWalletId(wallet.getWalletId());
        walletDto.setCurrency(wallet.getCurrency());
        walletDto.setBalance(wallet.getBalance());
        wallet.setMultiCurrency(wallet.isMultiCurrency());
        return walletDto;
    }
}
