package com.whatthefork.resourcereservation.resource.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;                // user table의 pk 참조하는 fk

    @Column(nullable = false)
    private Long resourceId;            // resource table의 pk 참조하는 fk

    @Column(nullable = false)
    private LocalDateTime bookedDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Builder
    public ResourceManagement(Long userId, Long resourceId, LocalDateTime bookedDate, LocalDateTime startDate, LocalDateTime endDate, int capacity, String reason) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.bookedDate = bookedDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.reason = reason;
    }

    public ResourceManagement updateBookedDate(LocalDateTime bookedDate) {
        this.bookedDate = bookedDate;
        return this;
    }

    public ResourceManagement updateStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public ResourceManagement updateEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public ResourceManagement updateReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ResourceManagement updateCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }
}
