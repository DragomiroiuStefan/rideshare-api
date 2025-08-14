package com.stefandragomiroiu.rideshare_api.security;

import com.stefandragomiroiu.rideshare_api.users.LoginResponse;
import com.stefandragomiroiu.rideshare_api.users.RegisterRequest;
import com.stefandragomiroiu.rideshare_api.users.RegisterResponse;
import com.stefandragomiroiu.rideshare_api.users.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public LoginResponse token(Authentication authentication) {
        logger.info("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken((MyUserDetails) authentication.getPrincipal());
        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registering user: {}", request);

        var registeredUser = userService.registerUser(request);

        String token = tokenService.generateToken(registeredUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.userId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new RegisterResponse(registeredUser, token));
    }

}
