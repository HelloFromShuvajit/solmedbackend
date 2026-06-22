package com.solmed.solmedbackend.dose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.solmed.solmedbackend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "dose_logs")
@Data
public class DoseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    /** Links to user_medicines.id for schedule and naming. */
    @Column(name = "user_medicine_id", nullable = false)
    private Long userMedicineId;

    @Column(nullable = false)
    private LocalTime scheduledTime;

    @Column
    private LocalDateTime takenAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DoseStatus status = DoseStatus.PENDING;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean alertSent = false;
}
