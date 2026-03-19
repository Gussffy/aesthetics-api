package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.dto.UserRequestDTO;
import com.gustavo.aestheticsapi.dto.UserResponseDTO;
import com.gustavo.aestheticsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        //Quando chegarmos na configuração do Spring Security, vamos corrigir isso usando BCryptPasswordEncoder. Por enquanto podemos deixar assim.
        user.setPassword(request.password());
        user.setRole(request.role());

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

    }

    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com id: " + id));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario não encontrado com id: " + id);
        }
        userRepository.deleteById(id);
    }
}
