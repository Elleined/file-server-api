package com.elleined.file_server_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FileServerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServerApiApplication.class, args);
    }
}
