package de.pretrendr.awscli;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.businesslogic.AWSCLIConnector;


public class AWSCLITest extends PretrendrTestBase{
	//TODO 
	

	@Test
	public void checkAWSCLIConnection() throws Exception{
		assertTrue("Bad AWS CLI credentials",(new AWSCLIConnector().startEMRCluster() != null));
	}
}
