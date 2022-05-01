package com.example.mvc1_servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Mvc1ServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mvc1ServletApplication.class, args);
	}

}
