package com.gustavo.aestheticsapi.domain.entity;

import com.gustavo.aestheticsapi.domain.enums.PaymentMethod;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn (name = "appointment_id")
    private Appointment appointment;

    @Column (nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull
    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paidAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
