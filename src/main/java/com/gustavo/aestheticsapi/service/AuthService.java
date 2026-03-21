package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.dto.AuthRequestDTO;
import com.gustavo.aestheticsapi.dto.AuthResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token);
    }
}
