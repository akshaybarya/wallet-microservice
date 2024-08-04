package com.akshaybarya.wallet.microservices.payments.v1.repository;

import com.akshaybarya.wallet.microservices.payments.v1.data.OrderDetails;
import com.akshaybarya.wallet.microservices.payments.v1.data.TRANSACTION_STATES;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentsRepository extends JpaRepository<OrderDetails, String> {
    OrderDetails findByTransactionId(String transactionId);
    List<OrderDetails> findByTransactionState(TRANSACTION_STATES transactionState);
}
