package com.akshaybarya.wallet.microservices.wallet_service.config;

import com.akshaybarya.wallet.microservices.wallet_service.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KafkaConstants.WALLET_BALANCE_TOPIC_NAME).build();
    }
}
