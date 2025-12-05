package com.whatthefork.resourcereservation.resource.dto.response;

import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceResponse(

    @NotNull
    Long id,

    @NotBlank
    String name,

    @Min(2)
    int maxCapacity,

    @NotNull
    boolean isBooked,

    @NotNull
    ResourceCategory category
) {
    public ResourceResponse(Resources resources) {
        this(
                resources.getId(),
                resources.getName(),
                resources.getMaxCapacity(),
                resources.isBooked(),
                resources.getCategory()
        );
    }
}