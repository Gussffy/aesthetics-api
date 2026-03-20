package com.gustavo.aestheticsapi.controller;

import com.gustavo.aestheticsapi.dto.UserRequestDTO;
import com.gustavo.aestheticsapi.dto.UserResponseDTO;
import com.gustavo.aestheticsapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create (@RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById (@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update (@PathVariable Long id, @RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
