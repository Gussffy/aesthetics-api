package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  // Metodo para encontrar todos os agendamentos de um cliente específico
  List<Appointment> findByClientId(Long userId);
}
