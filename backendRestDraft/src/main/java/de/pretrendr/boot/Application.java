package de.pretrendr.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tristan Schneider
 */
@SpringBootApplication(scanBasePackages = { "de.pretrendr" })
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
