package com.stefandragomiroiu.rideshare_api.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public record User(
        @Id
        Long userId,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthDate,
        String profilePicture,
        LocalDateTime createdAt,
        LocalDateTime lastLogin
) {
}

