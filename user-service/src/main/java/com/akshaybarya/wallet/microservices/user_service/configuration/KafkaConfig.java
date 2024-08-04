package com.akshaybarya.wallet.microservices.user_service.configuration;

import com.akshaybarya.wallet.microservices.user_service.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KafkaConstants.USER_DETAILS_TOPIC_NAME).build();
    }
}
