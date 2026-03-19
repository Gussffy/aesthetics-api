package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import com.gustavo.aestheticsapi.dto.AppointmentRequestDTO;
import com.gustavo.aestheticsapi.dto.AppointmentResponseDTO;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AestheticeServiceRepository aestheticeServiceRepository;

    public AppointmentResponseDTO create(AppointmentRequestDTO request) {

        User client = userRepository.findById(request.clientId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + request.clientId()));

        AestheticService service = aestheticeServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + request.serviceId()));

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setService(service);
        appointment.setScheduledAt(request.scheduledAt());

        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return new AppointmentResponseDTO(
                savedAppointment.getId(),
                savedAppointment.getClient().getName(),
                savedAppointment.getService().getName(),
                savedAppointment.getScheduledAt(),
                savedAppointment.getStatus()
        );
    }

    public AppointmentResponseDTO findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com id: " + id));

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getClient().getName(),
                appointment.getService().getName(),
                appointment.getScheduledAt(),
                appointment.getStatus()
        );
    }

    public List<AppointmentResponseDTO> findAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment -> new AppointmentResponseDTO(
                        appointment.getId(),
                        appointment.getClient().getName(),
                        appointment.getService().getName(),
                        appointment.getScheduledAt(),
                        appointment.getStatus()
                ))
                .toList();
    }

    public List<AppointmentResponseDTO> findByClientId(Long clientId) {

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + clientId));

        List<Appointment> appointments = appointmentRepository.findByClientId(clientId);
        return appointments.stream()
                .map(appointment -> new AppointmentResponseDTO(
                        appointment.getId(),
                        appointment.getClient().getName(),
                        appointment.getService().getName(),
                        appointment.getScheduledAt(),
                        appointment.getStatus()
                ))
                .toList();
    }

    public void cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com id: " + id));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

}
