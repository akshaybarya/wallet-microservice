package com.akshaybarya.wallet.microservices.wallet_service.config;

import com.akshaybarya.wallet.microservices.wallet_service.constants.KafkaConstants;
import com.akshaybarya.wallet.microservices.wallet_service.data.UserDetailsDto;
import com.akshaybarya.wallet.microservices.wallet_service.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class KafkaConsumerConfiguration {
    @Autowired
    private WalletService walletService;

    @KafkaListener(topics = KafkaConstants.USER_DETAILS_TOPIC_NAME, groupId = KafkaConstants.USER_DETAILS_CONSUMER_GROUP_ID)
    public void updateWalletData(String userDetailsDtoString) {
        log.info("Message received -> {}", userDetailsDtoString);
        try {
            UserDetailsDto userDetailsDto = new ObjectMapper().readValue(userDetailsDtoString, UserDetailsDto.class);
            walletService.createWallet(userDetailsDto.getUserId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
