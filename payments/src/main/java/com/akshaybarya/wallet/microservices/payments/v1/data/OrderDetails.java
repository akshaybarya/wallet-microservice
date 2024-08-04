package com.akshaybarya.wallet.microservices.payments.v1.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {
    @Id
    private String transactionId;

    @Min(value = 1L, message = "Minimum Transaction value should be 1 rupee")
    @Max(value = 10000000L, message = "Minimum Transaction value should be 10000000 rupee")
    @Column(updatable = false)
    private Long amount;

    @Column(unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private TRANSACTION_STATES transactionState = TRANSACTION_STATES.CREATED;

    @Column(updatable = false)
    @PastOrPresent
    private LocalDateTime createdAt = LocalDateTime.now();

    @PastOrPresent
    private LocalDateTime completedAt = null;
}
