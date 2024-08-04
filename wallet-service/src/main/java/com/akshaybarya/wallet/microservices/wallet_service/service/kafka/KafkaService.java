package com.akshaybarya.wallet.microservices.wallet_service.service.kafka;

import com.akshaybarya.wallet.microservices.wallet_service.constants.KafkaConstants;
import com.akshaybarya.wallet.microservices.wallet_service.data.WalletData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public boolean updateWalletBalance(WalletData walletData) {
        try {
            kafkaTemplate.send(KafkaConstants.WALLET_BALANCE_TOPIC_NAME, objectMapper.writeValueAsString(walletData));
            log.info("Message Produced -> {}", walletData);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
