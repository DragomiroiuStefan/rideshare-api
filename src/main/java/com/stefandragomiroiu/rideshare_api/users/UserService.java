package com.stefandragomiroiu.rideshare_api.users;

import com.stefandragomiroiu.rideshare_api.security.MyUserDetails;
import com.stefandragomiroiu.rideshare_api.web.exceptions.EmailAlreadyUsedException;
import com.stefandragomiroiu.rideshare_api.web.exceptions.ResourceNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MyUserDetails registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyUsedException("Email already registered");
        }

        User user = new User(
                null,
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.birthDate(),
                null,
                LocalDateTime.now(),
                null
        );

        user = userRepository.save(user);

        Role userRole = roleRepository.findByName(Roles.USER.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));

        roleRepository.assignRoleToUser(user.userId(), userRole.roleId(), user.userId());

        List<SimpleGrantedAuthority> authorities = permissionRepository.findAllByRoleId(userRole.roleId())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new MyUserDetails(user,  authorities);

    }

}
