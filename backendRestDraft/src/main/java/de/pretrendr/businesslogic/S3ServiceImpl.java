package de.pretrendr.businesslogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Maps;

@Service
public class S3ServiceImpl implements S3Service {
	final AmazonS3 s3;

	@Autowired
	public S3ServiceImpl() {
		// AWSCredentials credentials = new BasicAWSCredentials("accessKey",
		// "secretKey");
		s3 = AmazonS3ClientBuilder.standard()// .withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1).build();

	}

	@Override
	public Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException {
		Map<String, Integer> wordCounts = Maps.newHashMap();
		ObjectListing ol = s3.listObjects(bucket_name);
		List<S3ObjectSummary> objects = ol.getObjectSummaries(); // summary, contains names of files
		for (S3ObjectSummary os : objects) {
			S3Object s3object = s3.getObject(bucket_name, os.getKey()); // object, basically one of the files
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			File file = new File(os.getKey()); // actual file on local drive
			FileUtils.copyInputStreamToFile(inputStream, file); // save streamed data to file
			try (BufferedReader br = new BufferedReader(new FileReader(file))) { // reading the file again
				String line;
				while ((line = br.readLine()) != null) { // parsing word->count line by line
					String[] wordAndCount = line.split("\t");
					wordCounts.put(wordAndCount[0], Integer.parseInt(wordAndCount[1]));
				}
			}
			FileUtils.forceDelete(file);
		}
		return wordCounts;
	}

}
