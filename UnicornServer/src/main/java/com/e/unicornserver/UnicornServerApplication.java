package com.e.unicornserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class UnicornServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnicornServerApplication.class, args);
    }

}
