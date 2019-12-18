package com.cuixb.activiti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.activiti.spring.boot.SecurityAutoConfiguration.class)
public class App 
{
	 public static void main(String[] args) {
	        SpringApplication.run(App.class, args);
	 }
	
}
