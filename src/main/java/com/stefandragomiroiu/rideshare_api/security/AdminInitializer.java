package com.stefandragomiroiu.rideshare_api.security;

import com.stefandragomiroiu.rideshare_api.users.User;
import com.stefandragomiroiu.rideshare_api.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(1)
public class AdminInitializer {
//    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);
//
//    @Value("${admin.email}")
//    private String adminEmail;
//
//    @Value("${admin.default.password}")
//    private String defaultPassword;
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void initializeAdmin() {
//        if (userRepository.findByEmail(adminEmail).isEmpty()) {
//            logger.info("No admin user found. Creating default admin account...");
//
//            // Create admin if it doesn't exist
//            User adminUser = new User(
//                    null,
//                    adminEmail,
//                    passwordEncoder.encode(defaultPassword),
//                    "System",
//                    "Administrator",
//                    null,
//                    LocalDate.of(1970, 1, 1),
//                    null,
//                    LocalDateTime.now(),
//                    null
//            );
//
//            userRepository.save(adminUser);
//            logger.info("Default admin account created successfully");
//        }
//    }
}
