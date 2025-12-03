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
public class CanceledReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // resource_management table의 pk 참조하는 fk이자 현 테이블의 pk

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private LocalDateTime canceledDate;

    @Builder
    public CanceledReservation(String reason, LocalDateTime canceledDate) {
        this.reason = reason;
        this.canceledDate = canceledDate;
    }

    public CanceledReservation updateReason(String reason) {
        this.reason = reason;
        return this;
    }

    public CanceledReservation updateCanceledDate(LocalDateTime canceledDate) {
        this.canceledDate = canceledDate;
        return this;
    }
}
