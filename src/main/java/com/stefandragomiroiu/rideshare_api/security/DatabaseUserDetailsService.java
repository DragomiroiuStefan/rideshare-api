package com.stefandragomiroiu.rideshare_api.security;

import com.stefandragomiroiu.rideshare_api.users.PermissionRepository;
import com.stefandragomiroiu.rideshare_api.users.User;
import com.stefandragomiroiu.rideshare_api.users.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public DatabaseUserDetailsService(UserRepository userRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = permissionRepository.findAllByUserId(user.userId())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new MyUserDetails(user, authorities);
    }
}

