package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.EstablishmentRequestDTO;
import com.gustavo.aestheticsapi.dto.EstablishmentResponseDTO;
import com.gustavo.aestheticsapi.service.EstablishmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establishments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    @PostMapping
    public ResponseEntity<EstablishmentResponseDTO> create(@RequestBody @Valid EstablishmentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(establishmentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EstablishmentResponseDTO>> findAll() {
        return ResponseEntity.ok(establishmentService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstablishmentResponseDTO> update (
            @PathVariable Long id,
            @RequestBody @Valid EstablishmentRequestDTO request) {
        return ResponseEntity.ok(establishmentService.update(id, request));
    }
}
