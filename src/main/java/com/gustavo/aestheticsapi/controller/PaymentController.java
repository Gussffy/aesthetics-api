package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.PaymentRequestDTO;
import com.gustavo.aestheticsapi.dto.PaymentResponseDTO;
import com.gustavo.aestheticsapi.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody @Valid PaymentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(request));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponseDTO> findByAppointmentId(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.findByAppointmentId(appointmentId));
    }
}
