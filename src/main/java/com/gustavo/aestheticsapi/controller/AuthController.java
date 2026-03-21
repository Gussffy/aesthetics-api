package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.AuthRequestDTO;
import com.gustavo.aestheticsapi.dto.AuthResponseDTO;
import com.gustavo.aestheticsapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
