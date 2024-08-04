package com.akshaybarya.wallet.microservices.wallet_service.data;

import com.akshaybarya.wallet.microservices.wallet_service.constants.PG_TRANSACTION_STATE;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
public class OrderDetailsDto {
    private String transactionId;
    private Long amount;
    private String orderId;
    private PG_TRANSACTION_STATE transactionState;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
