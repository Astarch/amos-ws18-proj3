package de.pretrendr.boot.db;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Database configuration.
 * 
 * @author Tristan Schneider
 *
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = { "de.pretrendr" })
@EnableJpaRepositories(basePackages = { "de.pretrendr" })
@EnableTransactionManagement
public class DbConfig {
	@Bean
	@ConfigurationProperties("usermanagement.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public AmazonS3 amazonS3() {
		AmazonS3 s3;
		// AWSCredentials credentials = new BasicAWSCredentials("accessKey",
		// "secretKey");
		s3 = AmazonS3ClientBuilder.standard()// .withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1).build();
		return s3;
	}
}
