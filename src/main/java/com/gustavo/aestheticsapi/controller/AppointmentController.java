package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.AppointmentRequestDTO;
import com.gustavo.aestheticsapi.dto.AppointmentResponseDTO;
import com.gustavo.aestheticsapi.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@RequestBody @Valid AppointmentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> findByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.findByClientId(clientId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
