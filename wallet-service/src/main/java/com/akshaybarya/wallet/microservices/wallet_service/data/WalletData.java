package com.akshaybarya.wallet.microservices.wallet_service.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class WalletData {
    @Id
    private String walletId;

    @Column(updatable = false, unique = true)
    private String userId;

    @NotNull
    private BigInteger runningBalance = BigInteger.ZERO;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    private List<WalletTransaction> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return "WalletData{" +
                "userId='" + userId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", runningBalance=" + runningBalance +
                '}';
    }
}
