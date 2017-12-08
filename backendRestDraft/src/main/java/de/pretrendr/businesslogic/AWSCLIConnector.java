package de.pretrendr.businesslogic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = { "de.pretrendr" })
@EnableJpaRepositories(basePackages = { "de.pretrendr" })
@PropertySource("classpath:awscli.properties")
public class AWSCLIConnector {
	//TODO Florian: Doku + Clean up 
	
	@Value("${credentials.awscli.AWS_ACCESS_KEY_ID}")
	private String awscliAccessKey;

	@Value("${credentials.awscli.AWS_SECRET_ACCESS_KEY}")
	private String awscliSecredKey;
	
	@Bean
	public AmazonElasticMapReduceClient startEMRCluster(){
		System.out.println("#######################################");
		System.out.println(awscliAccessKey + " " + awscliSecredKey);
		BasicAWSCredentials credentials = new BasicAWSCredentials(awscliAccessKey,awscliSecredKey);
		return new AmazonElasticMapReduceClient(credentials);
	}
}
