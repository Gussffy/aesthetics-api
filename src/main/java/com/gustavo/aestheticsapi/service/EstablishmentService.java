package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.dto.EstablishmentRequestDTO;
import com.gustavo.aestheticsapi.dto.EstablishmentResponseDTO;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.EstablishmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;

    public EstablishmentResponseDTO create(EstablishmentRequestDTO request) {

        if (establishmentRepository.existsByEmail(request.email())) {
            throw new ConflictException("Já existe um estabelecimento cadastrado com o email: " + request.email());
        }

        Establishment establishment = new Establishment();
        establishment.setName(request.name());
        establishment.setEmail(request.email());
        establishment.setCnpj(request.cnpj());
        establishment.setDomain(request.domain());
        establishment.setActive(true);

        Establishment savedEstablishment = establishmentRepository.save(establishment);
        return new EstablishmentResponseDTO(
                savedEstablishment.getId(),
                savedEstablishment.getName(),
                savedEstablishment.getEmail(),
                savedEstablishment.getCnpj(),
                savedEstablishment.getDomain(),
                savedEstablishment.getActive()
        );
    }

    public EstablishmentResponseDTO findById(Long id) {
        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado com id: " + id));

        return new EstablishmentResponseDTO(
                establishment.getId(),
                establishment.getName(),
                establishment.getEmail(),
                establishment.getCnpj(),
                establishment.getDomain(),
                establishment.getActive()
        );
    }

    public List<EstablishmentResponseDTO> findAll() {

        List<Establishment> establishments = establishmentRepository.findAll();

        return establishments.stream()
                .map(est -> new EstablishmentResponseDTO(
                        est.getId(),
                        est.getName(),
                        est.getEmail(),
                        est.getCnpj(),
                        est.getDomain(),
                        est.getActive()
                ))
                .toList();
    }

    public EstablishmentResponseDTO update(Long id, EstablishmentRequestDTO request) {

        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado com id: " + id));

        establishment.setName(request.name());
        establishment.setEmail(request.email());
        establishment.setCnpj(request.cnpj());
        establishment.setDomain(request.domain());

        Establishment updatedEstablishment = establishmentRepository.save(establishment);
        return new EstablishmentResponseDTO(
                updatedEstablishment.getId(),
                updatedEstablishment.getName(),
                updatedEstablishment.getEmail(),
                updatedEstablishment.getCnpj(),
                updatedEstablishment.getDomain(),
                updatedEstablishment.getActive()
        );
    }
}
