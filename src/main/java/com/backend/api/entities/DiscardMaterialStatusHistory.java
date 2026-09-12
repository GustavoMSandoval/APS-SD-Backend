package com.backend.api.entities;

import com.backend.api.enums.DiscardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discard_material_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscardMaterialStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discard_material_id", nullable = false)
    private DiscardMaterial discardMaterial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscardStatus status;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
