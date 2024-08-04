package com.akshaybarya.wallet.microservices.user_service.service;

import com.akshaybarya.wallet.microservices.user_service.constants.KafkaConstants;
import com.akshaybarya.wallet.microservices.user_service.data.UserDetails;
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

    public boolean updateUserDetails(UserDetails userDetails) {
        try {
            kafkaTemplate.send(KafkaConstants.USER_DETAILS_TOPIC_NAME, objectMapper.writeValueAsString(userDetails));
            log.info("Message Produced -> {}", userDetails);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
