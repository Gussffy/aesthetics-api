package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.dto.AestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.service.AestheticServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AestheticServiceServiceTest {

    @Mock
    private AestheticeServiceRepository aestheticeServiceRepository;

    @InjectMocks
    private AestheticServiceService aestheticServiceService;

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

    // Teste para verificar se uma exceção é lançada quando o ID do serviço estético não é encontrado
    @Test
    void should_throw_exception_when_id_not_found() {

        when(aestheticeServiceRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            aestheticServiceService.findById(99L);
        });
    }
}
