package com.green.qna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QNAApplication {

	public static void main(String[] args) {
		SpringApplication.run(QNAApplication.class, args);
	}

}
