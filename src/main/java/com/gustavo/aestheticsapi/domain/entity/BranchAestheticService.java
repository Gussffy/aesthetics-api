package com.gustavo.aestheticsapi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "branch_services",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"branch_id", "service_id"},
                        name = "uk_branch_service"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class BranchAestheticService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private AestheticService service;

    @Column(nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
