package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.dto.BranchRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.BranchRepository;
import com.gustavo.aestheticsapi.repository.EstablishmentRepository;
import com.gustavo.aestheticsapi.service.BranchService;
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
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private EstablishmentRepository establishmentRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    void should_create_branch_successfully() {
        BranchRequestDTO request = new BranchRequestDTO(1L, "SP01", "Paulista", "Av Paulista", "paulista");

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(branchRepository.findByEstablishmentIdAndBranchCode(1L, "SP01")).thenReturn(Optional.empty());
        when(branchRepository.findBySubdomain("paulista")).thenReturn(Optional.empty());
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> {
            Branch branch = invocation.getArgument(0);
            branch.setId(10L);
            return branch;
        });

        BranchResponseDTO response = branchService.create(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.establishmentId()).isEqualTo(1L);
        assertThat(response.active()).isTrue();
    }

    @Test
    void should_throw_conflict_when_branch_code_already_exists_for_establishment() {
        BranchRequestDTO request = new BranchRequestDTO(1L, "SP01", "Paulista", "Av Paulista", "paulista");

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        Branch existing = new Branch();
        existing.setId(100L);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(branchRepository.findByEstablishmentIdAndBranchCode(1L, "SP01")).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> branchService.create(request));
    }

    @Test
    void should_throw_conflict_when_subdomain_already_exists() {
        BranchRequestDTO request = new BranchRequestDTO(1L, "SP01", "Paulista", "Av Paulista", "paulista");

        Establishment establishment = new Establishment();
        establishment.setId(1L);

        Branch existing = new Branch();
        existing.setId(100L);

        when(establishmentRepository.findById(1L)).thenReturn(Optional.of(establishment));
        when(branchRepository.findByEstablishmentIdAndBranchCode(1L, "SP01")).thenReturn(Optional.empty());
        when(branchRepository.findBySubdomain("paulista")).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> branchService.create(request));
    }

    @Test
    void should_throw_when_establishment_not_found_in_find_by_establishment_id() {
        when(establishmentRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> branchService.findByEstablishmentId(999L));
    }

    @Test
    void should_find_branches_by_establishment_id() {
        Establishment establishment = new Establishment();
        establishment.setId(1L);

        Branch branch = new Branch();
        branch.setId(10L);
        branch.setEstablishment(establishment);
        branch.setBranchCode("SP01");
        branch.setName("Paulista");
        branch.setAddress("Av Paulista");
        branch.setSubdomain("paulista");
        branch.setActive(true);

        when(establishmentRepository.existsById(1L)).thenReturn(true);
        when(branchRepository.findByEstablishmentId(1L)).thenReturn(List.of(branch));

        List<BranchResponseDTO> response = branchService.findByEstablishmentId(1L);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).branchCode()).isEqualTo("SP01");
    }

    @Test
    void should_update_branch_successfully() {
        BranchRequestDTO request = new BranchRequestDTO(2L, "RJ01", "Copacabana", "Av Atlantica", "copa");

        Establishment currentEstablishment = new Establishment();
        currentEstablishment.setId(1L);

        Branch branch = new Branch();
        branch.setId(10L);
        branch.setEstablishment(currentEstablishment);
        branch.setActive(true);

        Establishment newEstablishment = new Establishment();
        newEstablishment.setId(2L);

        when(branchRepository.findById(10L)).thenReturn(Optional.of(branch));
        when(establishmentRepository.findById(2L)).thenReturn(Optional.of(newEstablishment));
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BranchResponseDTO response = branchService.update(10L, request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.establishmentId()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Copacabana");
    }
}

