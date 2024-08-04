package com.akshaybarya.wallet.microservices.wallet_service.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDto {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private BigInteger walletBalance;
}
