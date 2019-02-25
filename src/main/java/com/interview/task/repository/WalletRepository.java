package com.interview.task.repository;

import com.interview.task.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents wallet repository interface.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
