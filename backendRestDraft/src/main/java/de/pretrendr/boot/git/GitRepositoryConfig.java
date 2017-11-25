package de.pretrendr.boot.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Provides information about the current state of git repo.
 */
@Data
@Configuration
public class GitRepositoryConfig {
	/**
	 * Retrieves information of current state of git repo based on
	 * <b>git.properties</b> which will be created by maven on build. For more
	 * configuration options, have a look at the maven plug in in <b>pom.xml</b>.
	 * 
	 * @return {@link GitRepositoryState} based on <b>git.properties</b>
	 * @throws IOException
	 */
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