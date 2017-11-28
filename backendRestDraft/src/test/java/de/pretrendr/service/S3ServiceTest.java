package de.pretrendr.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
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
import io.findify.s3mock.S3Mock;

@Transactional
public class S3ServiceTest extends PretrendrTestBase {

	private static final String UNCHANGEDBUCKETNAME = "unchanged-test-bucket-name";

	private static final String OTHERBUCKETNAME = "other-test-bucket-name";
	private static final String BUCKETNAME = "test-bucket-name";

	S3Mock api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();

	private static S3Service s3Service;

	private static AmazonS3 client = null;

	private CachedS3Bucket bucket;

	private CachedS3Bucket otherBucket;

	private CachedS3Bucket unchangedBucket;

	DateTime oneHourAgo = DateTime.now().minusHours(1);
	DateTime now = DateTime.now();
	DateTime twoHoursAgo = DateTime.now().minusHours(2);
	DateTime inOneHour = DateTime.now().plusHours(1);

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
			client.putObject(BUCKETNAME, "file0005createme",
					"word43\t1" + n + "word42\t2" + n + "word41\t3" + n + "word44\t4");

			client.createBucket(UNCHANGEDBUCKETNAME);
			client.putObject(UNCHANGEDBUCKETNAME, "file0001", "unchangedword1\t1" + n + "unchangedword2\t2");
			client.putObject(UNCHANGEDBUCKETNAME, "file0002",
					"unchangedword3\t3" + n + "unchangedword4\t4" + n + "unchangedword5\t5");

			s3Service = new S3ServiceImpl(client, cachedS3BucketDAO, cachedS3ObjectDAO, cachedS3WordCountPairDAO);
		}

		cachedS3BucketDAO.deleteAll();
		cachedS3ObjectDAO.deleteAll();
		cachedS3WordCountPairDAO.deleteAll();

		bucket = new CachedS3Bucket(BUCKETNAME, twoHoursAgo, oneHourAgo);
		bucket.getObjects()
				.addAll(Lists.newArrayList(new CachedS3Object(bucket, "file0001", twoHoursAgo, oneHourAgo),
						new CachedS3Object(bucket, "file0002", twoHoursAgo, oneHourAgo),
						new CachedS3Object(bucket, "file0003", twoHoursAgo, oneHourAgo),
						new CachedS3Object(bucket, "file0004", twoHoursAgo, oneHourAgo),
						new CachedS3Object(bucket, "file0005delme", twoHoursAgo, oneHourAgo)));
		bucket.getWordCount()
				.addAll(Lists.newArrayList(new CachedS3WordCountPair(bucket, "word1", 1),
						new CachedS3WordCountPair(bucket, "word2", 2), new CachedS3WordCountPair(bucket, "word3", 3),
						new CachedS3WordCountPair(bucket, "word4", 4), new CachedS3WordCountPair(bucket, "word5", 5)));
		this.bucket = cachedS3BucketDAO.save(bucket);

		otherBucket = new CachedS3Bucket(OTHERBUCKETNAME, twoHoursAgo, oneHourAgo);
		otherBucket.getObjects()
				.addAll(Lists.newArrayList(new CachedS3Object(otherBucket, "otherfile0001", twoHoursAgo, oneHourAgo),
						new CachedS3Object(otherBucket, "otherfile0002", twoHoursAgo, oneHourAgo),
						new CachedS3Object(bucket, "otherfile0003", twoHoursAgo, oneHourAgo),
						new CachedS3Object(otherBucket, "otherfile0004", twoHoursAgo, oneHourAgo),
						new CachedS3Object(otherBucket, "otherfile0005", twoHoursAgo, oneHourAgo)));
		otherBucket.getWordCount()
				.addAll(Lists.newArrayList(new CachedS3WordCountPair(otherBucket, "otherword1", 1),
						new CachedS3WordCountPair(otherBucket, "otherword2", 2),
						new CachedS3WordCountPair(otherBucket, "otherword3", 3),
						new CachedS3WordCountPair(otherBucket, "otherword4", 4),
						new CachedS3WordCountPair(otherBucket, "otherword5", 5)));
		this.otherBucket = cachedS3BucketDAO.save(otherBucket);

		unchangedBucket = new CachedS3Bucket(UNCHANGEDBUCKETNAME, twoHoursAgo, inOneHour);
		unchangedBucket.getObjects()
				.addAll(Lists.newArrayList(new CachedS3Object(unchangedBucket, "file0001", twoHoursAgo, inOneHour),
						new CachedS3Object(unchangedBucket, "file0002", twoHoursAgo, inOneHour)));
		unchangedBucket.getWordCount()
				.addAll(Lists.newArrayList(new CachedS3WordCountPair(unchangedBucket, "unchangedword1", 1),
						new CachedS3WordCountPair(unchangedBucket, "unchangedword2", 2),
						new CachedS3WordCountPair(unchangedBucket, "unchangedword3", 3),
						new CachedS3WordCountPair(unchangedBucket, "unchangedword4", 4),
						new CachedS3WordCountPair(unchangedBucket, "unchangedword5", 5)));
		this.unchangedBucket = cachedS3BucketDAO.save(unchangedBucket);
	}

	@Test
	public void updateExisting_changedBucket_checkValues() throws Exception {
		// arrange

		// act
		s3Service.updateCache(bucket.getId());
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(this.bucket.getId());

		// assert
		assertEquals(twoHoursAgo, bucket.getCreated());
		assertEquals(BUCKETNAME, bucket.getName());
		assertEquals(5, bucket.getObjects().size());
		assertEquals(20, bucket.getWordCount().size());
		assertNotNull(
				bucket.getObjects().stream().filter(o -> o.getName().equals("file0005createme")).findFirst().get());
		assertNull(
				bucket.getObjects().stream().filter(o -> o.getName().equals("file0005delme")).findFirst().orElse(null));
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("word11")).findFirst().get());
		assertNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("word5")).findFirst().orElse(null));
		assertEquals(true, bucket.isStillAvailable());
		assertNotEquals(oneHourAgo, bucket.getLastModified());
	}

	@Test
	public void updateExisting_noLongerAvailableBucket_checkValues() throws Exception {
		// arrange

		// act
		s3Service.updateCache(otherBucket.getId());
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(this.otherBucket.getId());

		// assert
		assertEquals(twoHoursAgo, bucket.getCreated());
		assertEquals(OTHERBUCKETNAME, bucket.getName());
		assertEquals(5, bucket.getObjects().size());
		assertEquals(5, bucket.getWordCount().size());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("otherfile0001")).findFirst().get());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("otherfile0002")).findFirst().get());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("otherfile0003")).findFirst().get());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("otherfile0004")).findFirst().get());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("otherfile0005")).findFirst().get());
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("otherword1")).findFirst().get());
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("otherword2")).findFirst().get());
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("otherword3")).findFirst().get());
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("otherword4")).findFirst().get());
		assertNotNull(bucket.getWordCount().stream().filter(o -> o.getWord().equals("otherword5")).findFirst().get());
		assertEquals(false, bucket.isStillAvailable());
		assertNotEquals(oneHourAgo, bucket.getLastModified());
	}

	@Test
	public void updateExisting_unchangedBucket_checkValues() throws Exception {
		// arrange

		// act
		s3Service.updateCache(unchangedBucket.getId());
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(this.unchangedBucket.getId());

		// assert
		assertEquals(twoHoursAgo, bucket.getCreated());
		assertEquals(UNCHANGEDBUCKETNAME, bucket.getName());
		assertEquals(2, bucket.getObjects().size());
		assertEquals(5, bucket.getWordCount().size());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("file0001")).findFirst().get());
		assertNotNull(bucket.getObjects().stream().filter(o -> o.getName().equals("file0002")).findFirst().get());
		assertNotNull(
				bucket.getWordCount().stream().filter(o -> o.getWord().equals("unchangedword1")).findFirst().get());
		assertNotNull(
				bucket.getWordCount().stream().filter(o -> o.getWord().equals("unchangedword2")).findFirst().get());
		assertNotNull(
				bucket.getWordCount().stream().filter(o -> o.getWord().equals("unchangedword3")).findFirst().get());
		assertNotNull(
				bucket.getWordCount().stream().filter(o -> o.getWord().equals("unchangedword4")).findFirst().get());
		assertNotNull(
				bucket.getWordCount().stream().filter(o -> o.getWord().equals("unchangedword5")).findFirst().get());
		assertEquals(true, bucket.isStillAvailable());
		assertNotEquals(inOneHour, bucket.getLastModified());
	}

	@Test
	public void getAllBuckets_valid_checkCount() throws Exception {
		mockMvc.perform(get("/api/s3/bucket/getAll")).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$", Matchers.hasSize(3)));
		// @formatter:on
	}

	@Test
	public void getBucket_validId_checkJSON() throws Exception {
		// arrange
		mockMvc.perform(get("/api/s3/bucket/" + bucket.getId())).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$.id", is(bucket.getId().toString())))
				.andExpect(jsonPath("$.name", is(bucket.getName())))
				.andExpect(jsonPath("$.created", notNullValue()))
				.andExpect(jsonPath("$.lastModified", notNullValue()));
		// @formatter:on
	}

	@Test
	public void deleteBucket_validId_checkJSON() throws Exception {
		// arrange
		mockMvc.perform(post("/api/s3/bucket/" + bucket.getId() + "/delete")).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$", is(true)));
		// @formatter:on

		mockMvc.perform(get("/api/s3/bucket/" + bucket.getId())).andExpect(status().isNotFound());
	}

	@Test
	public void createBucket_valid_checkJSON() throws Exception {
		// arrange
		String validBucketName = "test-bucket";

		// act
		mockMvc.perform(post("/api/s3/bucket/create/" + validBucketName)).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is(validBucketName)))
				.andExpect(jsonPath("$.created", notNullValue()))
				.andExpect(jsonPath("$.lastModified", nullValue()));
		// @formatter:on
	}

	@Test
	public void getWordCount_valid_checkJSON() throws Exception {
		// arrange

		// act
		mockMvc.perform(get("/api/s3/bucket/" + bucket.getId() + "/wordCount")).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$.content", notNullValue()))
				.andExpect(jsonPath("$.totalElements", is(bucket.getWordCount().size())))
				.andExpect(jsonPath("$.numberOfElements", is(5)))
				.andExpect(jsonPath("$.size", is(10)));
		// @formatter:on
	}

	@Test
	public void getAllObjects_valid_checkJSON() throws Exception {
		// arrange

		// act
		mockMvc.perform(get("/api/s3/bucket/" + bucket.getId() + "/object/getAll")).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$", Matchers.hasSize(5)));
		// @formatter:on
	}
}
