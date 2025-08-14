package com.stefandragomiroiu.rideshare_api.security;

import com.stefandragomiroiu.rideshare_api.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public record MyUserDetails(
        Long userId,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthDate,
        LocalDateTime lastLogin,
        List<SimpleGrantedAuthority> authorities
) implements UserDetails {

    public MyUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
        this(
                user.userId(),
                user.email(),
                user.password(),
                user.firstName(),
                user.lastName(),
                user.phoneNumber(),
                user.birthDate(),
                user.lastLogin(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}