package com.akshaybarya.wallet.microservices.user_service.service;

import com.akshaybarya.wallet.microservices.user_service.data.UserDetails;
import com.akshaybarya.wallet.microservices.user_service.helpers.UniqueIdGenerator;
import com.akshaybarya.wallet.microservices.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private KafkaService kafkaService;

    private final UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator("ACC", 8);

    public UserDetails getUserDetails(String userId) {
        var user = redisService.getUserById(userId).block();

        if (user != null) {
            log.info("Returning from redis -> {}", user);
            return user;
        }

        user = userRepository.findByUserId(userId);

        if (user != null) {
            redisService.saveUser(user).block();
            return user;
        }

        user = userRepository.findByEmail(userId);
        if (user != null) {
            redisService.saveUser(user).block();
            return user;
        }

        user = userRepository.findByPhoneNumber(userId);
        if (user != null) {
            redisService.saveUser(user).block();
        }

        return user;
    }

    public UserDetails createAUser(UserDetails userDetails) {
        userDetails.setUserId(uniqueIdGenerator.generateANewId());
        userDetails = userRepository.save(userDetails);
        kafkaService.updateUserDetails(userDetails);
        return userDetails;
    }

    public BigInteger getUserBalance(String userId) {
        return userRepository.findByUserId(userId).getWalletBalance();
    }

    public void updateUserBalance(String userId, BigInteger balance) {
        var userDetails = userRepository.findByUserId(userId);

        userDetails.setWalletBalance(balance);

        userRepository.save(userDetails);

        redisService.saveUser(userDetails).block();
    }
}
