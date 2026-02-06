package com.api.spring_marketplace_api.controller;

import com.api.spring_marketplace_api.model.dto.NewUserDto;
import com.api.spring_marketplace_api.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody NewUserDto newUserDto) {
        registerService.registerNewUser(
                newUserDto.email(),
                newUserDto.password(),
                newUserDto.name(),
                newUserDto.lastName()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User with email " + newUserDto.email() + " successfully created");
    }

    @PostMapping("/seller")
    public ResponseEntity<String> registerSeller(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");

        registerService.registerSeller(email);

        return ResponseEntity.ok("Now you are seller!");
    }
}
