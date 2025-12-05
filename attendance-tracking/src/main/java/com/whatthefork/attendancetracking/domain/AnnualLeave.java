package com.whatthefork.attendancetracking.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AnnualLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_leave", nullable = false)
    private Integer totalLeave;

    @Column(name = "used_leave", nullable = false)
    private Integer usedLeave = 0;

    @Column(name = "remaning_leave", nullable = false)
    private Integer remainingLeave = 0;

    @Column(name = "year", nullable = false)
    private Integer year;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public AnnualLeave(Long userId, Integer totalLeave, Integer usedLeave, Integer remainingLeave, Integer year) {
        this.userId = userId;
        this.totalLeave = totalLeave;
        this.usedLeave = usedLeave;
        this.remainingLeave = remainingLeave;
        this.year = year;
    }
}
