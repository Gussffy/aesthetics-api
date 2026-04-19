package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import com.gustavo.aestheticsapi.dto.BillingForecastResponseDTO;
import com.gustavo.aestheticsapi.dto.BillingPeriodResponseDTO;
import com.gustavo.aestheticsapi.dto.DashboardResponseDTO;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.PaymentRepository;
import com.gustavo.aestheticsapi.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void should_aggregate_dashboard_numbers() {
        Appointment pending = appointmentWithStatusAndPrice(AppointmentStatus.PENDING, "100.00");
        Appointment confirmed = appointmentWithStatusAndPrice(AppointmentStatus.CONFIRMED, "150.00");
        Appointment completed = appointmentWithStatusAndPrice(AppointmentStatus.COMPLETED, "200.00");
        Appointment cancelled = appointmentWithStatusAndPrice(AppointmentStatus.CANCELLED, "90.00");

        when(appointmentRepository.findByScheduledAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(pending, confirmed, completed, cancelled));
        when(paymentRepository.findTotalPaidToday(any(LocalDateTime.class), any(LocalDateTime.class), eq(PaymentStatus.PAID)))
                .thenReturn(new BigDecimal("220.00"));

        DashboardResponseDTO response = dashboardService.getDashboard();

        assertThat(response.revenueToday()).isEqualByComparingTo("220.00");
        assertThat(response.projectedRevenueToday()).isEqualByComparingTo("250.00");
        assertThat(response.appointmentsToday()).isEqualTo(4L);
        assertThat(response.pendingAppointments()).isEqualTo(1L);
        assertThat(response.confirmedAppointments()).isEqualTo(1L);
        assertThat(response.completedAppointments()).isEqualTo(1L);
        assertThat(response.cancelledAppointments()).isEqualTo(1L);
    }

    @Test
    void should_return_forecast_for_period() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);

        when(appointmentRepository.sumProjectedRevenue(any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(new BigDecimal("1200.00"));

        BillingForecastResponseDTO response = dashboardService.getForecast(start, end);

        assertThat(response.startDate()).isEqualTo(start);
        assertThat(response.endDate()).isEqualTo(end);
        assertThat(response.total()).isEqualByComparingTo("1200.00");

        verify(appointmentRepository).sumProjectedRevenue(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                argThat(statuses -> statuses.equals(List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED)))
        );
    }

    @Test
    void should_return_billing_period_values() {
        LocalDate start = LocalDate.of(2026, 2, 1);
        LocalDate end = LocalDate.of(2026, 2, 28);

        when(appointmentRepository.sumProjectedRevenueByPeriod(any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(new BigDecimal("3000.00"));
        when(paymentRepository.sumPaymentsByStatusAndPeriod(any(LocalDateTime.class), any(LocalDateTime.class), eq(PaymentStatus.PAID)))
                .thenReturn(new BigDecimal("1800.00"));
        when(paymentRepository.sumPaymentsByStatusAndPeriod(any(LocalDateTime.class), any(LocalDateTime.class), eq(PaymentStatus.PENDING)))
                .thenReturn(new BigDecimal("400.00"));

        BillingPeriodResponseDTO response = dashboardService.getBillingPeriod(start, end);

        assertThat(response.startDate()).isEqualTo(start);
        assertThat(response.endDate()).isEqualTo(end);
        assertThat(response.projectedRevenue()).isEqualByComparingTo("3000.00");
        assertThat(response.confirmedPayments()).isEqualByComparingTo("1800.00");
        assertThat(response.pendingPayments()).isEqualByComparingTo("400.00");
    }

    private Appointment appointmentWithStatusAndPrice(AppointmentStatus status, String price) {
        AestheticService service = new AestheticService();
        service.setPrice(new BigDecimal(price));

        Appointment appointment = new Appointment();
        appointment.setService(service);
        appointment.setStatus(status);
        return appointment;
    }
}


