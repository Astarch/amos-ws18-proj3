package de.pretrendr.boot.git;

import java.io.IOException;
import java.io.InputStream;
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
		InputStream stream = getClass().getClassLoader().getResourceAsStream("git.properties");
		if (stream != null) {
			properties.load(stream);
		}
		return new GitRepositoryState(properties);
	}
}