package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.AestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.AestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.service.AestheticServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class AestheticServiceController {

    private final AestheticServiceService aestheticServiceService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<AestheticServiceResponseDTO> create(@RequestBody @Valid AestheticServiceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aestheticServiceService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<AestheticServiceResponseDTO>> findAll() {
        return ResponseEntity.ok(aestheticServiceService.findAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('OWNER', 'CLIENT')")
    public ResponseEntity<List<AestheticServiceResponseDTO>> findAllActive() {
        return ResponseEntity.ok(aestheticServiceService.findAllActive());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'CLIENT')")
    public ResponseEntity<AestheticServiceResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(aestheticServiceService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<AestheticServiceResponseDTO> update (@PathVariable Long id, @RequestBody @Valid AestheticServiceRequestDTO request) {
        return ResponseEntity.ok(aestheticServiceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        aestheticServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
