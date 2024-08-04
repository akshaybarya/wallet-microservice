package com.akshaybarya.wallet.microservices.user_service.repository;

import com.akshaybarya.wallet.microservices.user_service.data.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, String> {
    UserDetails findByUserId(String userId);

    UserDetails findByPhoneNumber(String phoneNumber);

    UserDetails findByEmail(String email);
}
