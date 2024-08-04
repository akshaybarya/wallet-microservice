package com.akshaybarya.wallet.microservices.user_service.resource;

import com.akshaybarya.wallet.microservices.user_service.data.UserDetails;
import com.akshaybarya.wallet.microservices.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigInteger;
import java.net.URI;

@RestController
public class UserResource {
    @Autowired
    private UserService userService;

    @PostMapping("/v1/api/user")
    public ResponseEntity<UserDetails> createUser(@RequestBody @Valid UserDetails userDetails) {
        var user = userService.createAUser(userDetails);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getUserId()).toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/v1/api/user/{userId}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable String userId) {
        var user = userService.getUserDetails(userId);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/v1/api/user")
    public ResponseEntity<UserDetails> getUserDetailsByPhoneNumberOrEmail(
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "email" , required = false) String email
    ) {
        var identifier = phoneNumber != null ? phoneNumber : email;
        if (identifier == null) {
            return ResponseEntity.badRequest().build();
        }

        var user = userService.getUserDetails(identifier);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("v1/api/user/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        var user = userService.getUserDetails(userId);

        return ResponseEntity.ok().body(user != null);
    }

    @GetMapping("/v1/api/user/{userId}/update-balance/{balance}")
    public ResponseEntity<UserDetails> updateUserBalance(@PathVariable String userId, @PathVariable BigInteger balance) {
        userService.updateUserBalance(userId, balance);

        return ResponseEntity.accepted().body(userService.getUserDetails(userId));
    }
}
