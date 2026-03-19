package com.gustavo.aestheticsapi.domain.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "services")
@Getter
@Setter
@NoArgsConstructor
public class AestheticService {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column (nullable = false)
    private String name;

    @Column (length = 1000)
    private String description;

    @NotBlank
    @Column (nullable = false)
    private String category;

    @NotNull
    @Column (nullable = false)
    private BigDecimal price;

    @NotNull
    @Column (nullable = false)
    private Integer durationMinutes;

    @NotNull
    @Column (nullable = false)
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
