package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Payment;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Metodo para encontrar um pagamento pelo ID do agendamento
    Optional<Payment> findByAppointmentId(Long appointmentId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) " +
            "FROM Payment p " +
            "WHERE p.paymentStatus = :status " +
            "AND p.paidAt >= :start AND p.paidAt <= :end")
    BigDecimal findTotalPaidToday(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  @Param("status") PaymentStatus status);
}
