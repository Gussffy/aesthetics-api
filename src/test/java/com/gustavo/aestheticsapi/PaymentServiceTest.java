package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.entity.Payment;
import com.gustavo.aestheticsapi.domain.enums.PaymentMethod;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import com.gustavo.aestheticsapi.dto.PaymentRequestDTO;
import com.gustavo.aestheticsapi.dto.PaymentResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.PaymentRepository;
import com.gustavo.aestheticsapi.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void should_create_payment_using_service_price() {
        PaymentRequestDTO request = new PaymentRequestDTO(1L, PaymentMethod.PIX);

        AestheticService service = new AestheticService();
        service.setId(2L);
        service.setPrice(new BigDecimal("180.00"));

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setService(service);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setId(10L);
            return payment;
        });

        PaymentResponseDTO response = paymentService.create(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.appointmentId()).isEqualTo(1L);
        assertThat(response.paymentMethod()).isEqualTo(PaymentMethod.PIX);
        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(response.amount()).isEqualByComparingTo("180.00");

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        assertThat(captor.getValue().getAmount()).isEqualByComparingTo("180.00");
        assertThat(captor.getValue().getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void should_throw_when_appointment_not_found_on_create() {
        PaymentRequestDTO request = new PaymentRequestDTO(99L, PaymentMethod.CASH);

        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.create(request));
    }

    @Test
    void should_find_payment_by_appointment_id() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        Payment payment = new Payment();
        payment.setId(20L);
        payment.setAppointment(appointment);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setAmount(new BigDecimal("250.00"));

        when(paymentRepository.findByAppointmentId(1L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO response = paymentService.findByAppointmentId(1L);

        assertThat(response.id()).isEqualTo(20L);
        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(response.amount()).isEqualByComparingTo("250.00");
    }

    @Test
    void should_throw_when_payment_not_found_by_appointment_id() {
        when(paymentRepository.findByAppointmentId(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findByAppointmentId(999L));
    }
}

