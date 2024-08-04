package com.akshaybarya.wallet.microservices.user_service.configuration;

import com.akshaybarya.wallet.microservices.user_service.constants.KafkaConstants;
import com.akshaybarya.wallet.microservices.user_service.data.WalletDetailsDto;
import com.akshaybarya.wallet.microservices.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class KafkaConsumerConfiguration {
    @Autowired
    private UserService walletService;

    @KafkaListener(topics = KafkaConstants.WALLET_BALANCE_TOPIC_NAME, groupId = KafkaConstants.WALLET_BALANCE_CONSUMER_GROUP_ID)
    public void updateWalletData(String walletDetailsDtoString) {
        log.info("Message received -> {}", walletDetailsDtoString);
        try {
            WalletDetailsDto walletDetailsDto = new ObjectMapper().readValue(walletDetailsDtoString, WalletDetailsDto.class);
            walletService.updateUserBalance(walletDetailsDto.getUserId(), walletDetailsDto.getRunningBalance());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
