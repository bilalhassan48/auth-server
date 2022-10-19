package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Bilal Hassan on 11-Oct-2022
 * @project auth-ms
 */

@SpringBootApplication
@EnableCaching
public class AuthMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthMsApplication.class, args);
    }

}
