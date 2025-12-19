package com.stefandragomiroiu.rideshare_api.vehicles;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public record Vehicle(
        @Id
        Long plateNumber,

        @NotBlank
        String brand,

        @NotBlank
        String model,

        String color,

        @Min(2000)
        Integer year,

        @Min(1) @Max(7)
        Integer seats,

        Long ownerId

) {}