package com.akshaybarya.wallet.microservices.payments.v1.service;

import com.akshaybarya.wallet.microservices.payments.v1.data.OrderDetails;
import com.akshaybarya.wallet.microservices.payments.v1.data.TRANSACTION_STATES;
import com.akshaybarya.wallet.microservices.payments.v1.helpers.UniqueIdGenerator;
import com.akshaybarya.wallet.microservices.payments.v1.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentsService {
    @Autowired
    private PaymentsRepository paymentsRepository;

    private final UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator("PG0", 12);

    public OrderDetails createOrder(OrderDetails orderDetails) {
        orderDetails.setTransactionState(TRANSACTION_STATES.CREATED);
        orderDetails.setTransactionId(uniqueIdGenerator.generateANewId());

        if (paymentsRepository.findByTransactionId(orderDetails.getTransactionId()) == null) {
            paymentsRepository.save(orderDetails);
            return orderDetails;
        }

        return null;
    }

    public OrderDetails getOrderDetails(String transactionId) {
        return paymentsRepository.findByTransactionId(transactionId);
    }

    public OrderDetails updateOrderStatus(String transactionId, TRANSACTION_STATES transactionStatus) {
        var orderDetails = paymentsRepository.findByTransactionId(transactionId);

        if (orderDetails != null) {
            orderDetails.setTransactionState(transactionStatus);
            orderDetails.setCompletedAt(LocalDateTime.now());
            paymentsRepository.save(orderDetails);
            return orderDetails;
        }

        return null;
    }

    public List<OrderDetails> getAllPendingTransactions() {
        return paymentsRepository.findByTransactionState(TRANSACTION_STATES.CREATED);
    }
}
