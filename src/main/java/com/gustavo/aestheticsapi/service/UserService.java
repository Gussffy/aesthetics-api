package com.gustavo.aestheticsapi.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstablishmentRepository establishmentRepository;
    private final BranchRepository branchRepository;

    public UserResponseDTO create(UserRequestDTO request) {

        if (request.role() == UserRole.EMPLOYEE && request.branchId() == null) {
            throw new AppointmentException("Funcionário deve estar associado a uma filial");
        }

        Establishment establishment = establishmentRepository.findById(request.establishmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado com id: " + request.establishmentId()));

        Branch branch = null;
        if (request.branchId() != null) {
            branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com id: " + request.branchId()));

            if (!branch.getEstablishment().getId().equals(request.establishmentId())) {
                throw new ConflictException("A filial selecionada não pertence ao estabelecimento informado");
            }

        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setEstablishment(establishment);
        user.setBranch(branch);

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

    }

    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com id: " + id));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getRole()
        );
    }

    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com id: " + id));

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
            throw new ResourceNotFoundException("Usuario não encontrado com id: " + id);
        }
        userRepository.deleteById(id);
    }
}

