package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.dto.EstablishmentRequestDTO;
import com.gustavo.aestheticsapi.dto.EstablishmentResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.EstablishmentRepository;
import com.gustavo.aestheticsapi.service.EstablishmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstablishmentServiceTest {

    @Mock
    private EstablishmentRepository establishmentRepository;

    @InjectMocks
    private EstablishmentService establishmentService;

    @Test
    void should_create_establishment_with_active_true() {
        EstablishmentRequestDTO request = new EstablishmentRequestDTO(
                "Clinica Bela",
                "contato@clinicabela.com",
                "12345678000190",
                "clinicabela.com"
        );

        when(establishmentRepository.existsByEmail(request.email())).thenReturn(false);
        when(establishmentRepository.save(any(Establishment.class))).thenAnswer(invocation -> {
            Establishment establishment = invocation.getArgument(0);
            establishment.setId(1L);
            return establishment;
        });

        EstablishmentResponseDTO response = establishmentService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Clinica Bela");
        assertThat(response.active()).isTrue();
    }

    @Test
    void should_throw_conflict_when_email_already_exists() {
        EstablishmentRequestDTO request = new EstablishmentRequestDTO(
                "Clinica Bela",
                "contato@clinicabela.com",
                "12345678000190",
                "clinicabela.com"
        );

        when(establishmentRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(ConflictException.class, () -> establishmentService.create(request));
    }

    @Test
    void should_find_all_establishments() {
        Establishment establishment = new Establishment();
        establishment.setId(1L);
        establishment.setName("Clinica Bela");
        establishment.setEmail("contato@clinicabela.com");
        establishment.setCnpj("12345678000190");
        establishment.setDomain("clinicabela.com");
        establishment.setActive(true);

        when(establishmentRepository.findAll()).thenReturn(List.of(establishment));

        List<EstablishmentResponseDTO> response = establishmentService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).name()).isEqualTo("Clinica Bela");
    }

    @Test
    void should_throw_when_establishment_not_found() {
        when(establishmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> establishmentService.findById(999L));
    }

    @Test
    void should_update_establishment_successfully() {
        EstablishmentRequestDTO request = new EstablishmentRequestDTO(
                "Clinica Nova",
                "novo@clinica.com",
                "99999999000190",
                "novaclinica.com"
        );

        Establishment existing = new Establishment();
        existing.setId(5L);
        existing.setName("Antigo");
        existing.setEmail("antigo@clinica.com");
        existing.setCnpj("11111111000190");
        existing.setDomain("antigo.com");
        existing.setActive(true);

        when(establishmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(establishmentRepository.save(any(Establishment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EstablishmentResponseDTO response = establishmentService.update(5L, request);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.name()).isEqualTo("Clinica Nova");
        assertThat(response.email()).isEqualTo("novo@clinica.com");
    }
}

