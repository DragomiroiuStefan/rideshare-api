package com.stefandragomiroiu.rideshare_api.users;

import com.stefandragomiroiu.rideshare_api.security.MyUserDetails;

public record RegisterResponse(
        MyUserDetails user,
        String token
) {}

