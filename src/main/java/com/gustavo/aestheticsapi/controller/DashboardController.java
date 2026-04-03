package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.BillingPeriodResponseDTO;
import com.gustavo.aestheticsapi.dto.DashboardResponseDTO;
import com.gustavo.aestheticsapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }

    @GetMapping("/billing")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BillingPeriodResponseDTO> getBillingByPeriod(
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(dashboardService.getBillingPeriod(startDate, endDate));
    }

}
