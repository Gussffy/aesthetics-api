package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.domain.enums.UserRole;
import com.gustavo.aestheticsapi.dto.UserRequestDTO;
import com.gustavo.aestheticsapi.dto.UserResponseDTO;
import com.gustavo.aestheticsapi.exception.AppointmentException;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.BranchRepository;
import com.gustavo.aestheticsapi.repository.EstablishmentRepository;
import com.gustavo.aestheticsapi.repository.UserRepository;
import com.gustavo.aestheticsapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EstablishmentRepository establishmentRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private UserService userService;

    // Teste para verificar se um usuário é criado com sucesso
    @Test
    void should_create_user_successfully() {
        UserRequestDTO request = new UserRequestDTO(
                "Cliente teste",
                "client@email.com",
                "password123",
                UserRole.CLIENT,
                1L,
                null
        );

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setName("Cliente teste");
        fakeUser.setEmail("client@email.com");
        fakeUser.setPassword("encodedPassword");
        fakeUser.setRole(UserRole.CLIENT);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(fakeUser);

        UserResponseDTO response = userService.create(request);

        then(response.id()).isEqualTo(1L);
        then(response.name()).isEqualTo("Cliente teste");
        then(response.email()).isEqualTo("client@email.com");
        then(response.role()).isEqualTo(UserRole.CLIENT);
    }

    @Test
    void should_throw_when_employee_without_branch() {
        UserRequestDTO request = new UserRequestDTO(
                "Funcionario",
                "employee@email.com",
                "password123",
                UserRole.EMPLOYEE,
                1L,
                null
        );

        assertThrows(AppointmentException.class, () -> userService.create(request));
    }

    @Test
    void should_throw_when_branch_not_belongs_to_establishment() {
        UserRequestDTO request = new UserRequestDTO(
                "Funcionario",
                "employee@email.com",
                "password123",
                UserRole.EMPLOYEE,
                1L,
                2L
        );

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        Establishment otherEstablishment = new Establishment();
        otherEstablishment.setId(99L);

        Branch branch = new Branch();
        branch.setId(2L);
        branch.setEstablishment(otherEstablishment);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(branchRepository.findById(2L)).thenReturn(Optional.of(branch));

        assertThrows(ConflictException.class, () -> userService.create(request));
    }

    // Teste para verificar se um usuário é atualizado com sucesso
    @Test
    void should_update_user_successfully() {
        UserRequestDTO request = new UserRequestDTO(
                "Novo Nome",
                "novo@email.com",
                "newpass",
                UserRole.OWNER,
                1L,
                null
        );

        User existing = new User();
        existing.setId(10L);
        existing.setName("Antigo");
        existing.setEmail("old@email.com");
        existing.setRole(UserRole.CLIENT);

        when(userRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.update(10L, request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Novo Nome");
        assertThat(response.email()).isEqualTo("novo@email.com");
        assertThat(response.role()).isEqualTo(UserRole.OWNER);
    }

    // Teste para verificar se todos os usuários são retornados
    @Test
    void should_find_all_users() {
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("user1@email.com");
        user.setRole(UserRole.CLIENT);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> response = userService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).email()).isEqualTo("user1@email.com");
    }

    // Teste para verificar se um usuário é deletado com sucesso
    @Test
    void should_delete_user_successfully() {
        when(userRepository.existsById(20L)).thenReturn(true);

        userService.delete(20L);

        verify(userRepository).deleteById(20L);
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
