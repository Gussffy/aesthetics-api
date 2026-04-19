package com.gustavo.aestheticsapi;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import com.gustavo.aestheticsapi.domain.entity.Appointment;
import com.gustavo.aestheticsapi.domain.entity.Branch;
import com.gustavo.aestheticsapi.domain.entity.BranchAestheticService;
import com.gustavo.aestheticsapi.domain.entity.Establishment;
import com.gustavo.aestheticsapi.domain.entity.User;
import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import com.gustavo.aestheticsapi.dto.AppointmentRequestDTO;
import com.gustavo.aestheticsapi.dto.AppointmentResponseDTO;
import com.gustavo.aestheticsapi.exception.AppointmentException;
import com.gustavo.aestheticsapi.exception.ConflictException;
import com.gustavo.aestheticsapi.exception.ResourceNotFoundException;
import com.gustavo.aestheticsapi.exception.ServiceUnavailableException;
import com.gustavo.aestheticsapi.repository.AestheticeServiceRepository;
import com.gustavo.aestheticsapi.repository.AppointmentRepository;
import com.gustavo.aestheticsapi.repository.BranchAestheticServiceRepository;
import com.gustavo.aestheticsapi.repository.BranchRepository;
import com.gustavo.aestheticsapi.repository.UserRepository;
import com.gustavo.aestheticsapi.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AestheticeServiceRepository aestheticeServiceRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchAestheticServiceRepository branchAestheticServiceRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void should_create_appointment_with_pending_status() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        AppointmentRequestDTO request = new AppointmentRequestDTO(1L, 2L, 10L, 20L, scheduledAt);

        Establishment establishment = establishment(10L);
        Branch branch = branch(20L, establishment);
        User client = user(1L, "Cliente");
        AestheticService service = aestheticService(2L, "Limpeza");
        BranchAestheticService branchService = new BranchAestheticService();
        branchService.setId(99L);
        branchService.setBranch(branch);
        branchService.setService(service);
        branchService.setAvailable(true);

        when(branchRepository.findById(20L)).thenReturn(Optional.of(branch));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(aestheticeServiceRepository.findById(2L)).thenReturn(Optional.of(service));
        when(branchAestheticServiceRepository.findByBranchIdAndServiceId(20L, 2L)).thenReturn(Optional.of(branchService));
        when(appointmentRepository.findByBranchIdAndScheduledAtBetween(20L, scheduledAt, scheduledAt)).thenReturn(List.of());
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment toSave = invocation.getArgument(0);
            toSave.setId(100L);
            return toSave;
        });

        AppointmentResponseDTO response = appointmentService.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.clientName()).isEqualTo("Cliente");
        assertThat(response.serviceName()).isEqualTo("Limpeza");
        assertThat(response.status()).isEqualTo(AppointmentStatus.PENDING);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());
        assertThat(captor.getValue().getEstablishment().getId()).isEqualTo(10L);
        assertThat(captor.getValue().getBranch().getId()).isEqualTo(20L);
    }

    @Test
    void should_throw_when_scheduled_at_is_in_the_past() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                1L,
                2L,
                10L,
                20L,
                LocalDateTime.now().minusMinutes(1)
        );

        assertThrows(AppointmentException.class, () -> appointmentService.create(request));
    }

    @Test
    void should_throw_conflict_when_branch_not_belong_to_establishment() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                1L,
                2L,
                10L,
                20L,
                LocalDateTime.now().plusHours(2)
        );

        Establishment differentEstablishment = establishment(999L);
        Branch branch = branch(20L, differentEstablishment);

        when(branchRepository.findById(20L)).thenReturn(Optional.of(branch));

        assertThrows(ConflictException.class, () -> appointmentService.create(request));
    }

    @Test
    void should_throw_when_service_is_unavailable_in_branch() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                1L,
                2L,
                10L,
                20L,
                LocalDateTime.now().plusHours(2)
        );

        Establishment establishment = establishment(10L);
        Branch branch = branch(20L, establishment);
        User client = user(1L, "Cliente");
        AestheticService service = aestheticService(2L, "Limpeza");
        BranchAestheticService branchService = new BranchAestheticService();
        branchService.setId(99L);
        branchService.setBranch(branch);
        branchService.setService(service);
        branchService.setAvailable(false);

        when(branchRepository.findById(20L)).thenReturn(Optional.of(branch));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(aestheticeServiceRepository.findById(2L)).thenReturn(Optional.of(service));
        when(branchAestheticServiceRepository.findByBranchIdAndServiceId(20L, 2L)).thenReturn(Optional.of(branchService));

        assertThrows(ServiceUnavailableException.class, () -> appointmentService.create(request));
    }

    @Test
    void should_throw_when_client_already_has_conflicting_appointment() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        AppointmentRequestDTO request = new AppointmentRequestDTO(1L, 2L, 10L, 20L, scheduledAt);

        Establishment establishment = establishment(10L);
        Branch branch = branch(20L, establishment);
        User client = user(1L, "Cliente");
        AestheticService service = aestheticService(2L, "Limpeza");
        BranchAestheticService branchService = new BranchAestheticService();
        branchService.setBranch(branch);
        branchService.setService(service);
        branchService.setAvailable(true);

        Appointment conflict = new Appointment();
        conflict.setClient(client);
        conflict.setStatus(AppointmentStatus.CONFIRMED);

        when(branchRepository.findById(20L)).thenReturn(Optional.of(branch));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(aestheticeServiceRepository.findById(2L)).thenReturn(Optional.of(service));
        when(branchAestheticServiceRepository.findByBranchIdAndServiceId(20L, 2L)).thenReturn(Optional.of(branchService));
        when(appointmentRepository.findByBranchIdAndScheduledAtBetween(20L, scheduledAt, scheduledAt)).thenReturn(List.of(conflict));

        assertThrows(ConflictException.class, () -> appointmentService.create(request));
    }

    @Test
    void should_cancel_appointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.PENDING);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.cancel(1L);

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void should_throw_when_canceling_nonexistent_appointment() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.cancel(999L));
    }

    private Establishment establishment(Long id) {
        Establishment establishment = new Establishment();
        establishment.setId(id);
        establishment.setName("Estabelecimento");
        return establishment;
    }

    private Branch branch(Long id, Establishment establishment) {
        Branch branch = new Branch();
        branch.setId(id);
        branch.setName("Centro");
        branch.setEstablishment(establishment);
        return branch;
    }

    private User user(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    private AestheticService aestheticService(Long id, String name) {
        AestheticService service = new AestheticService();
        service.setId(id);
        service.setName(name);
        return service;
    }
}

