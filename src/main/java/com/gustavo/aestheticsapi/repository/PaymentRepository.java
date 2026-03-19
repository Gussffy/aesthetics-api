package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Metodo para encontrar um pagamento pelo ID do agendamento
    Optional<Payment> findByAppointmentId(Long appointmentId);
}
