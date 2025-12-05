package com.whatthefork.resourcereservation.resource.dto;

import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;

public record ResourcesDto (

    Long id,
    String name,
    int maxCapacity,
    boolean isBooked,
    ResourceCategory category
) {
    public ResourcesDto(Resources resources) {
        this(
                resources.getId(),
                resources.getName(),
                resources.getMaxCapacity(),
                resources.isBooked(),
                resources.getCategory()
        );
    }
}