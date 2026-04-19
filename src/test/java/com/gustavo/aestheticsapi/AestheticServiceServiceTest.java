package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.dto.AestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.AestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.repository.EstablishmentRepository;
import com.gustavo.aestheticsapi.service.AestheticServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AestheticServiceServiceTest {

    @Mock
    private AestheticeServiceRepository aestheticeServiceRepository;

    @Mock
    private EstablishmentRepository establishmentRepository;

    @InjectMocks
    private AestheticServiceService aestheticServiceService;

    // Teste para verificar se um serviço estético é criado com sucesso
    @Test
    void should_create_service_successfully() {
        AestheticServiceRequestDTO request = new AestheticServiceRequestDTO(
                1L,
                "Limpeza de Pele",
                "Limpeza profunda",
                "Facial",
                new BigDecimal("150.00"),
                60
        );

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(aestheticeServiceRepository.save(any(AestheticService.class))).thenAnswer(invocation -> {
            AestheticService service = invocation.getArgument(0);
            service.setId(1L);
            return service;
        });

        AestheticServiceResponseDTO response = aestheticServiceService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Limpeza de Pele");
        assertThat(response.active()).isTrue();
    }

    // Teste para verificar se um serviço estético é retornado corretamente quando o ID é válido
    @Test
    void should_return_service_when_id_is_valid() {

        AestheticService fakeService = new AestheticService();
        fakeService.setId(1L);
        fakeService.setName("Limpeza de Pele");
        fakeService.setDescription("Limpeza profunda da pele do rosto");
        fakeService.setCategory("Facial");
        fakeService.setPrice(new BigDecimal("150.00"));
        fakeService.setDurationMinutes(60);
        fakeService.setActive(true);

        when(aestheticeServiceRepository.findById(1L))
                .thenReturn(Optional.of(fakeService));

        AestheticServiceResponseDTO response = aestheticServiceService.findById(1L);

        assertThat(response.name()).isEqualTo("Limpeza de Pele");
        assertThat(response.active()).isTrue();
    }

    // Teste para verificar se um serviço estético é atualizado com sucesso
    @Test
    void should_update_service_successfully() {
        AestheticService existing = new AestheticService();
        existing.setId(1L);
        existing.setActive(true);

        AestheticServiceRequestDTO request = new AestheticServiceRequestDTO(
                1L,
                "Botox",
                "Aplicacao de botox",
                "Estetica",
                new BigDecimal("500.00"),
                40
        );

        when(aestheticeServiceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(aestheticeServiceRepository.save(any(AestheticService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AestheticServiceResponseDTO response = aestheticServiceService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Botox");
        assertThat(response.price()).isEqualByComparingTo("500.00");
    }

    // Teste para verificar se apenas serviços ativos são retornados
    @Test
    void should_return_only_active_services() {
        AestheticService active = new AestheticService();
        active.setId(1L);
        active.setName("Ativo");
        active.setDescription("Desc");
        active.setCategory("Cat");
        active.setPrice(new BigDecimal("10.00"));
        active.setDurationMinutes(10);
        active.setActive(true);

        when(aestheticeServiceRepository.findByActiveTrue()).thenReturn(List.of(active));

        List<AestheticServiceResponseDTO> response = aestheticServiceService.findAllActive();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).active()).isTrue();
    }

    // Teste para verificar se a exclusão lógica de um serviço estético define o ativo como falso
    @Test
    void should_soft_delete_service_setting_active_false() {
        AestheticService service = new AestheticService();
        service.setId(1L);
        service.setActive(true);

        when(aestheticeServiceRepository.findById(1L)).thenReturn(Optional.of(service));

        aestheticServiceService.delete(1L);

        assertThat(service.getActive()).isFalse();
        verify(aestheticeServiceRepository).save(service);
    }

    // Teste para verificar se uma exceção é lançada quando o ID do serviço estético não é encontrado
    @Test
    void should_throw_exception_when_id_not_found() {

        when(aestheticeServiceRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                aestheticServiceService.findById(99L));
    }
}
