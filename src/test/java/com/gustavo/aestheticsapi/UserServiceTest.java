package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.domain.enums.UserRole;
import com.gustavo.aestheticsapi.dto.UserRequestDTO;
import com.gustavo.aestheticsapi.dto.UserResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.UserRepository;
import com.gustavo.aestheticsapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Teste para verificar se um usuário é criado com sucesso
    @Test
    void should_create_user_successfully() {
        UserRequestDTO request = new UserRequestDTO(
                "Cliente teste",
                "client@email.com",
                "password123",
                UserRole.CLIENT
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setName("Cliente teste");
        fakeUser.setEmail("client@email.com");
        fakeUser.setPassword("encodedPassword");
        fakeUser.setRole(UserRole.CLIENT);

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(fakeUser);

        UserResponseDTO response = userService.create(request);

        then(response.id()).isEqualTo(1L);
        then(response.name()).isEqualTo("Cliente teste");
        then(response.email()).isEqualTo("client@email.com");
        then(response.role()).isEqualTo(UserRole.CLIENT);
    }

    // Teste para verificar se a exceção é lançada quando o usuário não é encontrado
    @Test
    void should_throw_exception_when_user_not_found() {

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(99L);
        });
    }
}
