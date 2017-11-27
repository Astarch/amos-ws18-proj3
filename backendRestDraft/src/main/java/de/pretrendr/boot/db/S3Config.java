package de.pretrendr.boot.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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
public class S3Config {
	@Value("${pretrendr.s3.accesskey}")
	private String s3AccessKey;

	@Value("${pretrendr.s3.secretkey}")
	private String s3SecredKey;

	private AmazonS3 s3;

	/**
	 * {@link AmazonS3} based on credentials provided in
	 * <b>application.properties<b>.
	 * 
	 * @return {@link AmazonS3} with configured credentials.
	 */
	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials credentials = s3CredentialsProvided();
		AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1);
		if (credentials != null) {
			s3Builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
		}

		s3 = s3Builder.build();
		return s3;
	}

	private AWSCredentials s3CredentialsProvided() {
		if (s3AccessKey != null && !s3AccessKey.isEmpty() && s3SecredKey != null && !s3SecredKey.isEmpty()) {
			return new BasicAWSCredentials(s3AccessKey, s3SecredKey);
		}
		return null;
	}
}
