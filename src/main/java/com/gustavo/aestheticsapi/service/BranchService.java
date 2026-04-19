package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.dto.BranchRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final EstablishmentRepository establishmentRepository;

    public BranchResponseDTO create(BranchRequestDTO request) {

        Establishment establishment = establishmentRepository.findById(request.establishmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado com id: " + request.establishmentId()));

        branchRepository.findByEstablishmentIdAndBranchCode(request.establishmentId(), request.branchCode())
                .ifPresent(b -> {
                    throw new ConflictException("Já existe uma filial com o código: " + request.branchCode() + " para este estabelecimento");
                });

        branchRepository.findBySubdomain(request.subdomain())
                .ifPresent(b -> {
                    throw new ConflictException("Já existe uma filial com o subdomínio: " + request.subdomain());
                });

        Branch branch = new Branch();
        branch.setEstablishment(establishment);
        branch.setBranchCode(request.branchCode());
        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setSubdomain(request.subdomain());
        branch.setActive(true);

        Branch savedBranch = branchRepository.save(branch);

        return new BranchResponseDTO(
                savedBranch.getId(),
                savedBranch.getEstablishment().getId(),
                savedBranch.getBranchCode(),
                savedBranch.getName(),
                savedBranch.getAddress(),
                savedBranch.getSubdomain(),
                savedBranch.getActive()
        );
    }

    public BranchResponseDTO findById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com id: " + id));

        return new BranchResponseDTO(
                branch.getId(),
                branch.getEstablishment().getId(),
                branch.getBranchCode(),
                branch.getName(),
                branch.getAddress(),
                branch.getSubdomain(),
                branch.getActive()
        );
    }

    public List<BranchResponseDTO> findByEstablishmentId(Long establishmentId) {

        if (!establishmentRepository.existsById(establishmentId)) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado com id: " + establishmentId);
        }

        List<Branch> branches = branchRepository.findByEstablishmentId(establishmentId);

        return branches.stream()
                .map(branch -> new BranchResponseDTO(
                        branch.getId(),
                        branch.getEstablishment().getId(),
                        branch.getBranchCode(),
                        branch.getName(),
                        branch.getAddress(),
                        branch.getSubdomain(),
                        branch.getActive()
                ))
                .toList();
    }

    public BranchResponseDTO update(Long id, BranchRequestDTO request) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com id: " + id));

        Establishment establishment = establishmentRepository.findById(request.establishmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado com id: " + request.establishmentId()));

        branch.setEstablishment(establishment);
        branch.setBranchCode(request.branchCode());
        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setSubdomain(request.subdomain());

        Branch updatedBranch = branchRepository.save(branch);

        return new BranchResponseDTO(
                updatedBranch.getId(),
                updatedBranch.getEstablishment().getId(),
                updatedBranch.getBranchCode(),
                updatedBranch.getName(),
                updatedBranch.getAddress(),
                updatedBranch.getSubdomain(),
                updatedBranch.getActive()
        );
    }
}
