package com.example.bibliotekbackenden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BibliotekbackendenApplication {
	public static void main(String[] args) {
		SpringApplication.run(BibliotekbackendenApplication.class, args);
	}
}