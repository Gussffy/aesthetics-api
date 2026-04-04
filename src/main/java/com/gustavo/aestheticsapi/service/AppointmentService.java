package com.gustavo.aestheticsapi.service;

import com.gustavo.aestheticsapi.domain.entity.*;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import com.gustavo.aestheticsapi.dto.AppointmentRequestDTO;
import com.gustavo.aestheticsapi.dto.AppointmentResponseDTO;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.exception.AppointmentException;
import com.gustavo.aestheticsapi.exception.ServiceUnavailableException;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AestheticeServiceRepository aestheticeServiceRepository;
    private final BranchRepository branchRepository;
    private final BranchServiceRepository branchServiceRepository;

    public AppointmentResponseDTO create(AppointmentRequestDTO request) {

        if (request.scheduledAt().isBefore(LocalDateTime.now())) {
            throw new AppointmentException("Não é permitido agendar em data/hora passada");
        }

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com id: " + request.branchId()));

        if (!branch.getEstablishment().getId().equals(request.establishmentId())) {
            throw new ConflictException("A filial selecionada não pertence ao estabelecimento informado");
        }

        User client = userRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + request.clientId()));

        AestheticService service = aestheticeServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + request.serviceId()));

        BranchService branchService = branchServiceRepository
                .findByBranchIdAndServiceId(request.branchId(), request.serviceId())
                .orElseThrow(() -> new ServiceUnavailableException("O serviço selecionado não está disponível nesta filial"));

        if (!branchService.getAvailable()) {
            throw new ServiceUnavailableException("Serviço selecionado não está disponível nesta filial");
        }

        List<Appointment> conflictingAppointments = appointmentRepository
                .findByBranchIdAndScheduledAtBetween(request.branchId(), request.scheduledAt(), request.scheduledAt())
                .stream()
                .filter(apt -> apt.getClient().getId().equals(request.clientId()))
                .filter(apt -> apt.getStatus() != AppointmentStatus.CANCELLED) // ignora cancelados
                .toList();

        if (!conflictingAppointments.isEmpty()) {
            throw new ConflictException("Cliente já possui agendamento neste horário");
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setService(service);
        appointment.setEstablishment(branch.getEstablishment());
        appointment.setBranch(branch);
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
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com id: " + id));

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

        if (!userRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + clientId);
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com id: " + id));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

}
