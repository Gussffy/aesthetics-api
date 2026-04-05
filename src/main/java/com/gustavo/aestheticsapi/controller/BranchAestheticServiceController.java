package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.BranchAestheticServiceRequestDTO;
import com.gustavo.aestheticsapi.dto.BranchAestheticServiceResponseDTO;
import com.gustavo.aestheticsapi.service.BranchAestheticServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branchs-services")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class BranchAestheticServiceController {

    private final BranchAestheticServiceService branchAestheticServiceService;

    @PostMapping
    public ResponseEntity<BranchAestheticServiceResponseDTO> create(@RequestBody @Valid BranchAestheticServiceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchAestheticServiceService.create(request));
    }

    @GetMapping("/branch/{branchId")
    public ResponseEntity<List<BranchAestheticServiceResponseDTO>> findByBranchIdAndAvailable(@PathVariable Long branchId) {
        return ResponseEntity.ok(branchAestheticServiceService.findByBranchIdAndAvailable(branchId));
    }

    @PatchMapping
    public ResponseEntity<Void> deactivate (@RequestParam Long id) {
        branchAestheticServiceService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}

