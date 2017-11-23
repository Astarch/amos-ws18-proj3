package de.pretrendr.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Tristan Schneider
 */
@SpringBootApplication(scanBasePackages = { "de.pretrendr" })
@EnableScheduling
public class Application {

	/**
	 * Application entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
