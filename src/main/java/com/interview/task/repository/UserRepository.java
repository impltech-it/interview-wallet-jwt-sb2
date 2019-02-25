package com.interview.task.repository;

import com.interview.task.dto.WalletDto;
import com.interview.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Represents user repository interface.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method search for user by email.
     *
     * @param email email
     * @return Optional<User>
     */
    Optional<User> findByEmail(final String email);

    /**
     * Method search for user by user's id.
     *
     * @param userId user id.
     * @return Optional<User>
     */
    Optional<User> findById(final Long userId);

    /**
     * Method search for user by username.
     *
     * @param username username
     * @return Optional<User>
     */
    Optional<User> findByUsername(final String username);

    /**
     * Method checks if user with username exists in database.
     *
     * @param username username
     * @return true if user exists, false otherwise
     */
    Boolean existsByUsername(final String username);

    /**
     * Method checks if user with email exists in database.
     *
     * @param email email
     * @return true if user exists, false otherwise
     */
    Boolean existsByEmail(final String email);

    /**
     * Method returns all user's wallets.
     *
     * @param userId user id.
     * @return List<WalletDto>
     */
    @Query("SELECT " +
            "NEW com.interview.task.dto.WalletDto(wal.walletId, wal.currency, wal.balance, wal.isMultiCurrency) " +
            "FROM User u " +
            "JOIN u.wallets wal " +
            "WHERE u.userId = :userId")
    List<WalletDto> getAllClientWallets(@Param("userId") final Long userId);
}
