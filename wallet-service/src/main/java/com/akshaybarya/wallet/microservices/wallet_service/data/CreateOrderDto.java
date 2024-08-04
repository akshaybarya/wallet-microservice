package com.akshaybarya.wallet.microservices.wallet_service.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
public class CreateOrderDto {
    private Long amount;
    private String orderId;
}
