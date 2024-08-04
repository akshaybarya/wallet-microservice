package com.akshaybarya.wallet.microservices.user_service.data;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletDetailsDto {
    private String userId;
    private String walletId;
    private BigInteger runningBalance;
}
