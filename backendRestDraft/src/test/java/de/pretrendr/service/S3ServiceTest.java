package de.pretrendr.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.common.collect.Lists;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.businesslogic.S3Service;
import de.pretrendr.businesslogic.S3ServiceImpl;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;
import de.pretrendr.model.CachedS3WordCountPair.CachedS3WordCountPairId;
import io.findify.s3mock.S3Mock;

public class S3ServiceTest extends PretrendrTestBase {

	private final String BUCKETNAME = "test-bucket-name";

	S3Mock api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();

	S3Service s3Service;

	AmazonS3 client = null;

	private CachedS3Bucket bucket;
	private List<CachedS3Object> objects;
	private List<CachedS3WordCountPairId> wordCountPairs;

	@Before
	public void setup() throws Exception {
		if (client == null) {
			EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:8001", "us-east-1");
			client = AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true)
					.withEndpointConfiguration(endpoint)
					.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials())).build();
			api.start();

			client.createBucket(BUCKETNAME);
			String n = System.lineSeparator();
			client.putObject(BUCKETNAME, "file0001",
					"word1\t1" + n + "word2\t2" + n + "word3\t3" + n + "word4\t4" + n + "");
			client.putObject(BUCKETNAME, "file0002",
					"word11\t1" + n + "word12\t2" + n + "word13\t3" + n + "word14\t4" + n);
			client.putObject(BUCKETNAME, "file0003", "word24\t1" + n + "word23\t2" + n + "word22\t3" + n + "word21\t4");
			client.putObject(BUCKETNAME, "file0004", "word31\t1" + n + "word34\t2" + n + "word32\t3" + n + "word33\t4");
			client.putObject(BUCKETNAME, "file0005", "word43\t1" + n + "word42\t2" + n + "word41\t3" + n + "word44\t4");
			s3Service = new S3ServiceImpl(client);
		}

		cachedS3BucketDAO.deleteAll();
		cachedS3ObjectDAO.deleteAll();
		cachedS3WordCountPairDAO.deleteAll();

		bucket = new CachedS3Bucket(BUCKETNAME);
		// cachedS3BucketDAO.save(new CachedS3Bucket(BUCKETNAME));

		bucket.getObjects()
				.addAll(Lists.newArrayList(new CachedS3Object(this.bucket, "file0001"),
						new CachedS3Object(this.bucket, "file0002"), new CachedS3Object(this.bucket, "file0003"),
						new CachedS3Object(this.bucket, "file0004")));

		bucket.getWordCount()
				.addAll(Lists.newArrayList(new CachedS3WordCountPair(bucket, "word1", 1),
						new CachedS3WordCountPair(bucket, "word2", 2), new CachedS3WordCountPair(bucket, "word3", 3),
						new CachedS3WordCountPair(bucket, "word4", 4), new CachedS3WordCountPair(bucket, "word5", 5)));

		this.bucket = cachedS3BucketDAO.save(bucket);
	}

	@Test
	public void test() throws Exception {
		s3Service.updateAllBuckets();
	}
}
