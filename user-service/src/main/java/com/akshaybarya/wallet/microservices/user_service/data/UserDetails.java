package com.akshaybarya.wallet.microservices.user_service.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDetails {
    @Id
    @Column(updatable = false)
    private String userId;

    @Min(value = 5, message = "Please enter a name with more than 5 letters")
    @Column(nullable = false)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Min(value = 10, message = "Please enter a valid mobile number")
    @Max(value = 10, message = "Please enter a valid mobile number")
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private BigInteger walletBalance = BigInteger.ZERO;
}
