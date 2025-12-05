package com.whatthefork.resourcereservation.resource.dto;

import com.whatthefork.resourcereservation.resource.entity.CanceledReservation;

import java.time.LocalDateTime;

public record CanceledReservationDto(

        Long id,
        String reason,
        LocalDateTime canceledDate
) {
    public CanceledReservationDto(CanceledReservation reservation) {
        this(
                reservation.getId(),
                reservation.getReason(),
                reservation.getCanceledDate()
        );
    }
}
