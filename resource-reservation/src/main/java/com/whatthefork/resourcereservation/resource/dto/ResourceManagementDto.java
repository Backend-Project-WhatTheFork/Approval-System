package com.whatthefork.resourcereservation.resource.dto;

import com.whatthefork.resourcereservation.resource.entity.ResourceManagement;

import java.time.LocalDateTime;

public record ResourceManagementDto (

    Long id,
    Long userId,
    Long resourceId,
    LocalDateTime bookedDate,
    LocalDateTime startDate,
    LocalDateTime endDate,
    int capacity,
    String reason
) {
    public ResourceManagementDto(ResourceManagement management) {
        this(
                management.getId(),
                management.getUserId(),
                management.getResourceId(),
                management.getBookedDate(),
                management.getStartDate(),
                management.getEndDate(),
                management.getCapacity(),
                management.getReason()
        );
    }
}