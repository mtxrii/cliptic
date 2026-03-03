package com.mtxrii.cliptic.clipticbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClipticBackendApplication {

    static void main(String[] args) {
        System.out.println("Running on url: " + ClipticConst.REDIRECT_BASE_URL);
        SpringApplication.run(ClipticBackendApplication.class, args);
    }

}
