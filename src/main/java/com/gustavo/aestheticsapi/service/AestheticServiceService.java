package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.dto.AestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.AestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AestheticServiceService {

    private final AestheticeServiceRepository serviceRepository;

    public AestheticServiceResponseDTO create(AestheticServiceRequestDTO request){

        AestheticService service = new AestheticService();

        service.setName(request.name());
        service.setDescription(request.description());
        service.setCategory(request.category());
        service.setPrice(request.price());
        service.setDurationMinutes(request.durationMinutes());

        AestheticService savedService = serviceRepository.save(service);
        return new AestheticServiceResponseDTO(
                savedService.getId(),
                savedService.getName(),
                savedService.getDescription(),
                savedService.getCategory(),
                savedService.getPrice(),
                savedService.getDurationMinutes(),
                savedService.getActive()
        );
    }

    public AestheticServiceResponseDTO findById(Long id) {
        AestheticService service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + id));

        return new AestheticServiceResponseDTO(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getCategory(),
                service.getPrice(),
                service.getDurationMinutes(),
                service.getActive()
        );
    }

    public List<AestheticServiceResponseDTO> findAllActive() {
        List<AestheticService> services = serviceRepository.findByActiveTrue();
        return services.stream()
                .map(service -> new AestheticServiceResponseDTO(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getCategory(),
                        service.getPrice(),
                        service.getDurationMinutes(),
                        service.getActive()
                ))
                .toList();
    }

    public List<AestheticServiceResponseDTO> findAll() {
        List<AestheticService> services = serviceRepository.findAll();
        return services.stream()
                .map(service -> new AestheticServiceResponseDTO(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getCategory(),
                        service.getPrice(),
                        service.getDurationMinutes(),
                        service.getActive()
                ))
                .toList();
    }

    public void delete(Long id) {
        AestheticService service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + id));

        service.setActive(false);
        serviceRepository.save(service);
    }
}
