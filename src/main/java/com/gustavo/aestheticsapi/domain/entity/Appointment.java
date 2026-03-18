package com.gustavo.aestheticsapi.domain.entity;

import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table (name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn (name = "service_id")
    private Service service;

    @Column (nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated (EnumType.STRING)
    private AppointmentStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
