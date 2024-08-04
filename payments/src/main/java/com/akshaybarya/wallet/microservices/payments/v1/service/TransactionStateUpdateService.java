package com.akshaybarya.wallet.microservices.payments.v1.service;

import com.akshaybarya.wallet.microservices.payments.v1.data.OrderDetails;
import com.akshaybarya.wallet.microservices.payments.v1.data.TRANSACTION_STATES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class TransactionStateUpdateService {
    @Autowired
    private PaymentsService paymentsService;

    private TRANSACTION_STATES deriveTransactionStateRandomly() {
        return Math.random() < 0.5 ? TRANSACTION_STATES.SUCCESS : TRANSACTION_STATES.FAILED;
    }

    public List<OrderDetails> updateTransactionState() {
        var pendingTransactions = paymentsService.getAllPendingTransactions();
        var updatedTransactions = new ArrayList<OrderDetails>();

        if (pendingTransactions != null) {
            pendingTransactions.forEach(transaction -> {
                var transactionSate = deriveTransactionStateRandomly();
                var updatedOrderDetails = paymentsService.updateOrderStatus(transaction.getTransactionId(), transactionSate);
                if (updatedOrderDetails == null) {
                    log.error("Unable to update transaction state for -> {}", transaction);
                } else {
                    updatedTransactions.add(updatedOrderDetails);
                }
            });
        }

        log.info("Updated Transactions -> {}", updatedTransactions);
        return updatedTransactions;
    }
}
