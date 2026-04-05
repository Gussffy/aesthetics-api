package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.BranchAestheticService;
import com.gustavo.aestheticsapi.dto.BranchAestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchAestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.repository.BranchRepository;
import com.gustavo.aestheticsapi.repository.BranchAestheticServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchAestheticServiceService {

    private final BranchAestheticServiceRepository branchAestheticServiceRepository;
    private final BranchRepository branchRepository;
    private final AestheticeServiceRepository aestheticeServiceRepository;

    public BranchAestheticServiceResponseDTO create(BranchAestheticServiceRequestDTO request) {

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com id: " + request.branchId()));

        AestheticService aestheticService = aestheticeServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + request.serviceId()));

        branchAestheticServiceRepository.findByBranchIdAndServiceId(request.branchId(), request.serviceId())
                .ifPresent(bs -> {
                    throw new ConflictException("O serviço já está associado a esta filial");
                });

        BranchAestheticService branchAestheticService = new BranchAestheticService();
        branchAestheticService.setBranch(branch);
        branchAestheticService.setService(aestheticService);
        branchAestheticService.setAvailable(true);

        BranchAestheticService savedBranchAestheticService = branchAestheticServiceRepository.save(branchAestheticService);

        return new BranchAestheticServiceResponseDTO(
                savedBranchAestheticService.getId(),
                savedBranchAestheticService.getBranch().getId(),
                savedBranchAestheticService.getService().getId(),
                savedBranchAestheticService.getBranch().getName(),
                savedBranchAestheticService.getAvailable()
        );
    }

    public List<BranchAestheticServiceResponseDTO> findByBranchIdAndAvailable(Long branchId) {

        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Filial não encontrada com id: " + branchId);
        }

        List<BranchAestheticService> branchAestheticServices = branchAestheticServiceRepository.findByBranchIdAndAvailableTrue(branchId);

        return branchAestheticServices.stream()
                .map(bs -> new BranchAestheticServiceResponseDTO(
                        bs.getId(),
                        bs.getBranch().getId(),
                        bs.getService().getId(),
                        bs.getBranch().getName(),
                        bs.getAvailable()
                )).toList();
    }

    public void deactivate(Long id) {
        BranchAestheticService branchAestheticService = branchAestheticServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Associação de serviço e filial não encontrada com id: " + id));

        branchAestheticService.setAvailable(false);
        BranchAestheticService updatedBranchAestheticService = branchAestheticServiceRepository.save(branchAestheticService);
    }
}
