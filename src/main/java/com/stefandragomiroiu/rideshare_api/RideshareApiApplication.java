package com.stefandragomiroiu.rideshare_api;

import com.stefandragomiroiu.rideshare_api.web.CustomizedErrorAttributes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RideshareApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideshareApiApplication.class, args);
    }

    @Bean
    public CustomizedErrorAttributes errorAttributes() {
        return new CustomizedErrorAttributes();
    }
}
