package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.entity.Payment;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import com.gustavo.aestheticsapi.dto.PaymentRequestDTO;
import com.gustavo.aestheticsapi.dto.PaymentResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    public PaymentResponseDTO create(PaymentRequestDTO request) {

        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com id: " + request.appointmentId()));


        Payment payment = new Payment();

        payment.setAppointment(appointment);
        payment.setPaymentMethod(request.paymentMethod());
        payment.setAmount(appointment.getService().getPrice());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentResponseDTO(
                savedPayment.getId(),
                savedPayment.getAppointment().getId(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaymentStatus(),
                savedPayment.getAmount(),
                savedPayment.getPaidAt());
    }

    public PaymentResponseDTO findByAppointmentId(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado para o agendamento com id: " + appointmentId));

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getAppointment().getId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getAmount(),
                payment.getPaidAt()
        );
    }
}