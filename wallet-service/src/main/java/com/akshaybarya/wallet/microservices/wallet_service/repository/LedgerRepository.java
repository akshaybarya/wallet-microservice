package com.akshaybarya.wallet.microservices.wallet_service.repository;

import com.akshaybarya.wallet.microservices.wallet_service.data.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepository extends JpaRepository<WalletTransaction, String> {
    WalletTransaction findByTransactionId(String transactionId);

    List<WalletTransaction> findAllByWallet_WalletId(String walletId);
}
