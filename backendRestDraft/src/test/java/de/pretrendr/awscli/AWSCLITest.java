package de.pretrendr.awscli;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import de.pretrendr.PretrendrTestBase;

public class AWSCLITest extends PretrendrTestBase{
	
	@Value("${credentials.awscli.AWS_ACCESS_KEY_ID}")
	private String awscliAccessKey;

	@Value("${credentials.secretawscli.AWS_SECRET_ACCESS_KEY}")
	private String awscliSecredKey;

	@Test
	public void checkAWSCLIConnection() throws Exception{
		BasicAWSCredentials credentials = new BasicAWSCredentials(awscliAccessKey,awscliSecredKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		String bucketName = "javatutorial-net-example-bucket";
		s3client.createBucket(bucketName);
		assertTrue("Bad AWS CLI credentials",(credentials.getAWSAccessKeyId() != null));
	}
}
