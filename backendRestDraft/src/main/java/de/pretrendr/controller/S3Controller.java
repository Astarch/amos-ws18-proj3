package de.pretrendr.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.businesslogic.S3Service;
import de.pretrendr.ex.InvalidBucketNameException;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

@RequestMapping("/api/s3")
@RestController
@CrossOrigin
@Transactional
public class S3Controller {
	@Autowired
	S3Service s3Service;

	/**
	 * Default Items-Per-Page. This is a string, because the request expects an
	 * input of type String. It will be parsed by the framework.
	 */
	public static final String DEFAULT_IPP = "10";

	@RequestMapping(value = "/bucket/{bucketId}/wordCount", method = RequestMethod.GET)
	public ResponseEntity<Page<CachedS3WordCountPair>> getWordCountByBucketId(@PathVariable("bucketId") UUID bucketId,
			@RequestParam(value = "number", defaultValue = "0") final int number,
			@RequestParam(value = "size", defaultValue = DEFAULT_IPP) final int size) throws IOException {
		Page<CachedS3WordCountPair> list = s3Service.getWordCount(bucketId, number, size);

		return new ResponseEntity<Page<CachedS3WordCountPair>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Bucket>> getAllBuckets() throws IOException {
		List<CachedS3Bucket> bucketList = s3Service.getAllBuckets();

		return new ResponseEntity<List<CachedS3Bucket>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<CachedS3Bucket> getBucket(@PathVariable("bucketId") UUID bucketId) throws IOException {
		CachedS3Bucket bucketList = s3Service.getBucket(bucketId);

		return new ResponseEntity<CachedS3Bucket>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/create/{bucketName}", method = RequestMethod.POST)
	public ResponseEntity<CachedS3Bucket> createBucket(@PathVariable("bucketName") String bucketName)
			throws IOException, InvalidBucketNameException {
		CachedS3Bucket bucket = s3Service.createBucket(bucketName);

		return new ResponseEntity<CachedS3Bucket>(bucket, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/{bucketId}/delete", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deleteBucket(@PathVariable("bucketId") UUID bucketId)
			throws IOException, InvalidBucketNameException {
		boolean success = s3Service.deleteBucket(bucketId);

		return new ResponseEntity<Boolean>(success, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/{bucketId}/object/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Object>> getAllObjectsByBucketId(@PathVariable("bucketId") final UUID bucketId)
			throws IOException {
		List<CachedS3Object> bucketList = s3Service.getAllObjects(bucketId);

		return new ResponseEntity<List<CachedS3Object>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/{bucketId}/update", method = RequestMethod.GET)
	public ResponseEntity<CachedS3Bucket> updateCacheByBucket(@PathVariable("bucketId") UUID bucketId)
			throws IOException {
		CachedS3Bucket result = s3Service.updateCache(bucketId);

		return new ResponseEntity<CachedS3Bucket>(result, HttpStatus.OK);
	}
}
