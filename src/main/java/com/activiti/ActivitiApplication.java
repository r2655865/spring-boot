package com.activiti;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { org.activiti.spring.boot.SecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
@MapperScan("com.activiti.mapper")
public class ActivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
	}
}
