package com.stefandragomiroiu.rideshare_api.users;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Permission(
        @Id
        Long permissionId,
        String name,
        Long createdBy,
        LocalDateTime createdAt
) {
}
