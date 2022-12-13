package com.clinica.odontologica;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Dentist Clinic API",
		version = "1.0",
		description = "Managment system of dentists, patients and turns"
))
public class OdontologicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdontologicaApplication.class, args);
	}

}
