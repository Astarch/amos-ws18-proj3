package de.pretrendr.boot.git;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class GitRepositoryConfig {
	@Bean
	public GitRepositoryState gitRepositoryState() throws IOException {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));

		return new GitRepositoryState(properties);
	}
}