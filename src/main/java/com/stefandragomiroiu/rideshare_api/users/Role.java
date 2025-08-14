package com.stefandragomiroiu.rideshare_api.users;

import com.stefandragomiroiu.rideshare_api.web.validation.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public record Role(
    @Id
    Long roleId,
    @NotBlank
    @Size(max = 50)
    String name,
    @NotNull
    boolean active
) {}
