package com.akshaybarya.wallet.microservices.wallet_service.resource;

import com.akshaybarya.wallet.microservices.wallet_service.data.OrderDetailsDto;
import com.akshaybarya.wallet.microservices.wallet_service.data.WalletData;
import com.akshaybarya.wallet.microservices.wallet_service.data.WalletTransaction;
import com.akshaybarya.wallet.microservices.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletResource {
    @Autowired
    private WalletService walletService;

    @GetMapping("/v1/user/{userId}/wallet")
    public ResponseEntity<WalletData> getUserWallet(@PathVariable String userId) {
        var walletData = walletService.getUserWallet(userId);

        if (walletData != null) {
            return ResponseEntity.ok().body(walletData);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/v1/user/{userId}/transaction/{transactionId}")
    public ResponseEntity<WalletTransaction> getTransactionDetail(@PathVariable String userId, @PathVariable String transactionId) {
        var transactionData = walletService.getTransactionDetails(transactionId);

        if (transactionData.getWallet().getUserId().equals(userId)) {
            return ResponseEntity.ok().body(transactionData);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/v1/user/{userId}/transactions")
    public ResponseEntity<List<WalletTransaction>> getAllTransactions(@PathVariable String userId) {
        return ResponseEntity.ok().body(walletService.getTransactionsForAUser(userId));
    }

    @PostMapping("/v1/user/{userId}/create")
    public ResponseEntity<WalletTransaction> createOrder(@RequestBody @Valid WalletTransaction walletTransaction, @PathVariable String userId) {
        try {
            return ResponseEntity.status(201).body(walletService.createTransaction(walletTransaction, userId));
        } catch (Exception e) {
           return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/v1/callback/update-transaction")
    public ResponseEntity<Unknown> updateOrderStatusFromPg(@RequestBody @Valid OrderDetailsDto pgOrderStatusResponse) {
        walletService.updateTransactionStatusAndBalance(pgOrderStatusResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/user/{userId}/initialize-wallet")
    public ResponseEntity<WalletData> createWallet(@PathVariable String userId) {
        return ResponseEntity.status(201).body(walletService.createWallet(userId));
    }
}
