package com.hyagohenrique.ferias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FeriasApplication {

	public static void main(final String[] args) {
		SpringApplication.run(FeriasApplication.class, args);
	}

}
