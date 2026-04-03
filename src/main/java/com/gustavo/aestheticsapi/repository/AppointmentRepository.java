package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  // Metodo para encontrar todos os agendamentos de um cliente específico
  List<Appointment> findByClientId(Long userId);

  List<Appointment> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);

  @Query("SELECT COALESCE(SUM(s.price), 0) " +
          "FROM Appointment a " +
          "JOIN a.service s " +
          "WHERE a.scheduledAt BETWEEN :start AND :end " +
          "AND a.status IN :statuses")
  BigDecimal sumProjectedRevenue(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("statuses") List<AppointmentStatus> statuses);

  @Query("SELECT COALESCE(SUM(s.price), 0) " +
          "FROM Appointment a " +
          "JOIN a.service s " +
          "WHERE a.scheduledAt >= :start AND a.scheduledAt <= :end " +
          "AND a.status IN :statuses")
  BigDecimal sumProjectedRevenueByPeriod(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("statuses") List<AppointmentStatus> statuses);
}
