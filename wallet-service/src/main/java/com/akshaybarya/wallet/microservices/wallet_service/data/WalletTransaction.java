package com.akshaybarya.wallet.microservices.wallet_service.data;

import com.akshaybarya.wallet.microservices.wallet_service.constants.TRANSACTION_STATE;
import com.akshaybarya.wallet.microservices.wallet_service.constants.TRANSACTION_TYPE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "wallet-transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletTransaction {
    @Id
    private String transactionId;

    @Column
    @JsonIgnore
    private String pgReferenceId;

    @Min(value = 0L, message = "Amount should be greater that 0")
    private Long amount;

    @Enumerated(EnumType.STRING)
    private TRANSACTION_TYPE transactionType;

    @Enumerated(EnumType.STRING)
    private TRANSACTION_STATE transactionState = TRANSACTION_STATE.PENDING;

    @PastOrPresent
    private LocalDateTime createdAt = LocalDateTime.now();

    @PastOrPresent
    private LocalDateTime completedAt = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id") // You may need to specify the column name if it's not default
    @JsonIgnore
    private WalletData wallet;
}
