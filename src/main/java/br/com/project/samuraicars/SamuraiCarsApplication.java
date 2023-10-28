package br.com.project.samuraicars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SamuraiCarsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SamuraiCarsApplication.class, args);
	}

}
