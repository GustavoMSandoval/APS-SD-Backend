package com.backend.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;

import java.time.LocalDateTime;

@Entity
@Table(name = "discard_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscardMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscardType type;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime discardDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 255)
    private String employeeWhoDiscarded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}