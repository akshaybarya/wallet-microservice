package com.akshaybarya.wallet.microservices.payments.v1.helpers;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;

@AllArgsConstructor
public class UniqueIdGenerator {
    private final String prefix;
    private final int size;

    private static final SecureRandom random = new SecureRandom();

    public String generateANewId() {
        var randomNumberLength = size - prefix.length();

        int number = random.nextInt((int) Math.pow(10, randomNumberLength));
        String numberStr = String.format("%0" + randomNumberLength + "d", number);
        return prefix + numberStr;
    }
}
