package de.pretrendr.awscli;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class AWSCLITest{

	@Test
	public void checkAWSCLIConnection() throws Exception{
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		assertTrue("Bad AWS CLI credentials",(credentialsProvider.getCredentials() != null));
	}
}
