package de.pretrendr.service;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.businesslogic.S3Service;

public class S3ServiceTest extends PretrendrTestBase {

	@Autowired
	S3Service s3Service;

	@Test
	public void test() throws IOException {
		// // arrange
		//
		// // act
		// Map<String, Integer> map =
		// s3Service.getWorkdCountMapFromBucketName("amos-pretrendr-commoncrawl-sample");
		//
		// // assert
		// assertEquals(29164, map.size());
	}
}
