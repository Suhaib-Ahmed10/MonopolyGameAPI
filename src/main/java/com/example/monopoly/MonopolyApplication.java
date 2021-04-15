package com.example.monopoly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages="com.example.monopoly")
@SpringBootApplication
public class MonopolyApplication {


	public static void main(String[] args) {
		SpringApplication.run(MonopolyApplication.class, args);
	}

}
