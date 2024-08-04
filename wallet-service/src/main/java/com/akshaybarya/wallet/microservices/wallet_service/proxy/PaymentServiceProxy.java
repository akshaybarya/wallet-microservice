package com.akshaybarya.wallet.microservices.wallet_service.proxy;

import com.akshaybarya.wallet.microservices.wallet_service.data.CreateOrderDto;
import com.akshaybarya.wallet.microservices.wallet_service.data.OrderDetailsDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service", url = "localhost:5000")
public interface PaymentServiceProxy {
    @PostMapping("/v1/api/create-order")
    public ResponseEntity<OrderDetailsDto> createPaymentOrder(@Valid @RequestBody CreateOrderDto orderDetails);

    @GetMapping("/v1/api/order-status/{transactionId}")
    public ResponseEntity<OrderDetailsDto> getPaymentOrderDetails(@PathVariable String transactionId);
}
