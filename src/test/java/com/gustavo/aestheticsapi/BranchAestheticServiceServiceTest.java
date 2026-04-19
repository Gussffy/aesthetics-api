package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.BranchAestheticService;
import com.gustavo.aestheticsapi.dto.BranchAestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchAestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.repository.BranchAestheticServiceRepository;
import com.gustavo.aestheticsapi.repository.BranchRepository;
import com.gustavo.aestheticsapi.service.BranchAestheticServiceService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchAestheticServiceServiceTest {

    @Mock
    private BranchAestheticServiceRepository branchAestheticServiceRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private AestheticeServiceRepository aestheticeServiceRepository;

    @InjectMocks
    private BranchAestheticServiceService service;

    @Test
    void should_create_branch_service_association_with_available_true() {
        BranchAestheticServiceRequestDTO request = new BranchAestheticServiceRequestDTO(1L, 2L);

        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Centro");

        AestheticService aestheticService = new AestheticService();
        aestheticService.setId(2L);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(aestheticeServiceRepository.findById(2L)).thenReturn(Optional.of(aestheticService));
        when(branchAestheticServiceRepository.findByBranchIdAndServiceId(1L, 2L)).thenReturn(Optional.empty());
        when(branchAestheticServiceRepository.save(any(BranchAestheticService.class))).thenAnswer(invocation -> {
            BranchAestheticService entity = invocation.getArgument(0);
            entity.setId(50L);
            return entity;
        });

        BranchAestheticServiceResponseDTO response = service.create(request);

        assertThat(response.id()).isEqualTo(50L);
        assertThat(response.branchId()).isEqualTo(1L);
        assertThat(response.serviceId()).isEqualTo(2L);
        assertThat(response.available()).isTrue();
    }

    @Test
    void should_throw_conflict_when_association_already_exists() {
        BranchAestheticServiceRequestDTO request = new BranchAestheticServiceRequestDTO(1L, 2L);

        Branch branch = new Branch();
        branch.setId(1L);

        AestheticService aestheticService = new AestheticService();
        aestheticService.setId(2L);

        BranchAestheticService existing = new BranchAestheticService();
        existing.setId(999L);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(aestheticeServiceRepository.findById(2L)).thenReturn(Optional.of(aestheticService));
        when(branchAestheticServiceRepository.findByBranchIdAndServiceId(1L, 2L)).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> service.create(request));
    }

    @Test
    void should_throw_when_listing_available_services_for_nonexistent_branch() {
        when(branchRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.findByBranchIdAndAvailable(10L));
    }

    @Test
    void should_list_available_services_by_branch() {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Centro");

        AestheticService aestheticService = new AestheticService();
        aestheticService.setId(2L);

        BranchAestheticService association = new BranchAestheticService();
        association.setId(100L);
        association.setBranch(branch);
        association.setService(aestheticService);
        association.setAvailable(true);

        when(branchRepository.existsById(1L)).thenReturn(true);
        when(branchAestheticServiceRepository.findByBranchIdAndAvailableTrue(1L)).thenReturn(List.of(association));

        List<BranchAestheticServiceResponseDTO> response = service.findByBranchIdAndAvailable(1L);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).available()).isTrue();
    }

    @Test
    void should_deactivate_association() {
        BranchAestheticService association = new BranchAestheticService();
        association.setId(30L);
        association.setAvailable(true);

        when(branchAestheticServiceRepository.findById(30L)).thenReturn(Optional.of(association));

        service.deactivate(30L);

        assertThat(association.getAvailable()).isFalse();
        verify(branchAestheticServiceRepository).save(association);
    }
}

