package com.gustavo.aestheticsapi.dto;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal revenueToday,
        BigDecimal projectedRevenueToday,
        Long appointmentsToday,
        Long pendingAppointments,
        Long confirmedAppointments,
        Long completedAppointments,
        Long cancelledAppointments
) {
}
