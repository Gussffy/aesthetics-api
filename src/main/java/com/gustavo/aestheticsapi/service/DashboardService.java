package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import com.gustavo.aestheticsapi.dto.DashboardResponseDTO;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;

    public DashboardResponseDTO getDashboard() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Appointment> todayAppointments = appointmentRepository
                .findByScheduledAtBetween(startOfDay, endOfDay);

        BigDecimal revenueToday = paymentRepository.findTotalPaidToday(startOfDay, endOfDay, PaymentStatus.PAID);

        BigDecimal projectedRevenueToday = todayAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.PENDING
                        || a.getStatus() == AppointmentStatus.CONFIRMED)
                .map(a -> a.getService().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pending = todayAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.PENDING).count();
        long confirmed = todayAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED).count();
        long completed = todayAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
        long cancelled = todayAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

        return new DashboardResponseDTO(
                revenueToday,
                projectedRevenueToday,
                (long) todayAppointments.size(),
                pending,
                confirmed,
                completed,
                cancelled
        );
    }
}
