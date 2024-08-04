package com.akshaybarya.wallet.microservices.wallet_service.repository;

import com.akshaybarya.wallet.microservices.wallet_service.data.WalletData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletData, String> {
    WalletData findByUserId(String userId);

    WalletData findByWalletId(String walletId);
}
