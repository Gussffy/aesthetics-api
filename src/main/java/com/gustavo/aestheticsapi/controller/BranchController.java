package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.BranchRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchResponseDTO;
import com.gustavo.aestheticsapi.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponseDTO> create(@RequestBody @Valid BranchRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @GetMapping("/establishment/{establishmentId}")
    public ResponseEntity<List<BranchResponseDTO>> findByEstablishmentId(@PathVariable Long establishmentId) {
        return ResponseEntity.ok(branchService.findByEstablishmentId(establishmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid BranchRequestDTO request) {
        return ResponseEntity.ok(branchService.update(id, request));
    }
}