package com.akshaybarya.wallet.microservices.payments.v1.resource;

import com.akshaybarya.wallet.microservices.payments.v1.data.OrderDetails;
import com.akshaybarya.wallet.microservices.payments.v1.data.TRANSACTION_STATES;
import com.akshaybarya.wallet.microservices.payments.v1.service.PaymentsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentsResource {
    @Autowired
    private PaymentsService paymentsService;

    @PostMapping("/v1/api/create-order")
    public ResponseEntity<OrderDetails> createPaymentOrder(@Valid @RequestBody OrderDetails orderDetails) {
        var paymentContext = paymentsService.createOrder(orderDetails);

        if (paymentContext != null) {
            return ResponseEntity.status(201).body(paymentContext);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/v1/api/order-status/{transactionId}")
    public ResponseEntity<OrderDetails> getPaymentOrderDetails(@PathVariable String transactionId) {
        var orderDetails = paymentsService.getOrderDetails(transactionId);

        if (orderDetails != null) {
            return ResponseEntity.ok().body(orderDetails);
        }

        return ResponseEntity.badRequest().build();
    }

//    @GetMapping("/v1/test/{transactionId}")
//    public void updateOrder(@PathVariable String transactionId) {
//        paymentsService.updateOrderStatus(transactionId, TRANSACTION_STATES.SUCCESS);
//    }
//
//    @GetMapping("/v1/all-pending-txns")
//    public List<OrderDetails> getAllPendingTxn() {
//        return paymentsService.getAllPendingTransactions();
//    }
}
