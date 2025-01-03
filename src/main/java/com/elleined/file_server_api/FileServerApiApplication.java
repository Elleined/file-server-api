package com.elleined.file_server_api;

import org.apache.tika.Tika;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FileServerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileServerApiApplication.class, args);
	}

	@Bean
	public Tika tika() {
		return new Tika();
	}
}
