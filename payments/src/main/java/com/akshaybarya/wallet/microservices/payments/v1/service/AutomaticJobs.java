package com.akshaybarya.wallet.microservices.payments.v1.service;

import com.akshaybarya.wallet.microservices.payments.v1.data.OrderDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class AutomaticJobs {

    @Autowired
    private TransactionStateUpdateService transactionStateUpdateService;

    @Value("${transaction-update-cron.interval}")
    private long transactionUpdateCronInterval;

    @Value("${merchant-webhook-url}")
    private String merchantWebhookUrl;

    @Scheduled(fixedRateString = "${transaction-update-cron.interval}")
    public void transactionJob() {
        log.info("Running Update Txn Cron"); // Correct logging statement
        var updatedTransactions = transactionStateUpdateService.updateTransactionState();

        sendNotificationForOrders(updatedTransactions);
    }

    private void sendNotificationForOrders(List<OrderDetails> orders) {
        var restTemplate = new RestTemplate();

        log.info("Sending Webhook for -> {}", orders);
        orders.forEach(orderDetails -> {
            try {
                restTemplate.postForEntity(merchantWebhookUrl, orderDetails, ResponseEntity.class);
                log.info("Webhook triggered for -> {}", orderDetails);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }
}
