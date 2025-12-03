package com.whatthefork.attendancetracking.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "punch_in", nullable = true)
    private LocalDateTime punchInDate;

    @Column(name = "punch_out", nullable = true)
    private LocalDateTime punchOutDate;

    @Column(name = "is_late", nullable = false)
    private boolean isLate;

    @Column(name = "overtime_minutes", nullable = false)
    private Integer overtimeMinutes;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Attendance(Long userId, LocalDateTime punchInDate, LocalDateTime punchOutDate, boolean isLate, Integer overtimeMinutes, LocalDate workDate) {
        this.userId = userId;
        this.punchInDate = punchInDate;
        this.punchOutDate = punchOutDate;
        this.isLate = isLate;
        this.overtimeMinutes = overtimeMinutes;
        this.workDate = workDate;
    }
}
