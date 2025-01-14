package ca.homefinance.homefinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HomefinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomefinanceApplication.class, args);
	}
}
