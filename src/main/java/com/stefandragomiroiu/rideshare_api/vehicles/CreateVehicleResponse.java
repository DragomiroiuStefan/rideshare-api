package com.stefandragomiroiu.rideshare_api.vehicles;

public record CreateVehicleResponse(
        Vehicle vehicle,
        String errorMessage
) {}

