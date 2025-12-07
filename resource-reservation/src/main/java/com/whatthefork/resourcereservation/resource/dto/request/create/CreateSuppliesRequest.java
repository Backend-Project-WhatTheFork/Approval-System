package com.whatthefork.resourcereservation.resource.dto.request.create;

import com.whatthefork.resourcereservation.resource.entity.CorporateCar;
import com.whatthefork.resourcereservation.resource.entity.Supplies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateSuppliesRequest (

        @NotBlank
        String name,

        @Min(1)
        int capacity
) {
    public CreateSuppliesRequest(Supplies supplies) {
        this(
                supplies.getName(),
                supplies.getCapacity()
        );
    }
}
