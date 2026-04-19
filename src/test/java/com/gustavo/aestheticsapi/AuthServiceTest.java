package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.dto.AuthRequestDTO;
import com.gustavo.aestheticsapi.dto.AuthResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.UserRepository;
import com.gustavo.aestheticsapi.security.JwtService;
import com.gustavo.aestheticsapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void should_login_and_return_jwt_token() {
        AuthRequestDTO request = new AuthRequestDTO("user@email.com", "123456");
        User user = new User();
        user.setId(1L);
        user.setEmail("user@email.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(request);

        assertThat(response.token()).isEqualTo("jwt-token");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("user@email.com");
        assertThat(captor.getValue().getCredentials()).isEqualTo("123456");
    }

    @Test
    void should_throw_when_user_not_found_after_authentication() {
        AuthRequestDTO request = new AuthRequestDTO("missing@email.com", "123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        when(userRepository.findByEmail("missing@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }
}

