package de.pretrendr.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/wordCountMap/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Integer>> getDummyGraphData(@PathVariable(name = "bucketname") String bucketname)
			throws IOException {
		Map<String, Integer> map = s3Service.getWordCountMapFromBucketName(bucketname);

		return new ResponseEntity<Map<String, Integer>>(map, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordCountMapByBucketName/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3WordCountPair>> getWordCountByBucketName(
			@PathVariable("bucketname") String bucketname) throws IOException {
		List<CachedS3WordCountPair> list = s3Service.getWordCountMapByBucket(bucketname);

		return new ResponseEntity<List<CachedS3WordCountPair>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordCountMapByBucketId/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3WordCountPair>> getWordCountByBucketId(@PathVariable("bucketId") UUID bucketId)
			throws IOException {
		List<CachedS3WordCountPair> list = s3Service.getWordCountMapByBucket(bucketId);

		return new ResponseEntity<List<CachedS3WordCountPair>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Bucket>> getAllBuckets() throws IOException {
		List<CachedS3Bucket> bucketList = s3Service.getAllBuckets();

		return new ResponseEntity<List<CachedS3Bucket>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/create/{bucketName}", method = RequestMethod.POST)
	public ResponseEntity<CachedS3Bucket> createBucket(@PathVariable("bucketName") String bucketName)
			throws IOException, InvalidBucketNameException {
		CachedS3Bucket bucket = s3Service.createBucket(bucketName);

		return new ResponseEntity<CachedS3Bucket>(bucket, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/deleteByName/{bucketName}", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deleteBucket(@PathVariable("bucketName") String bucketName)
			throws IOException, InvalidBucketNameException {
		boolean success = s3Service.deleteBucket(bucketName);

		return new ResponseEntity<Boolean>(success, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/deleteById/{bucketId}", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deleteBucket(@PathVariable("bucketId") UUID bucketId)
			throws IOException, InvalidBucketNameException {
		boolean success = s3Service.deleteBucket(bucketId);

		return new ResponseEntity<Boolean>(success, HttpStatus.OK);
	}

	@RequestMapping(value = "/object/getAllByBucketId/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Object>> getAllObjectsByBucketId(@PathVariable("bucketId") final UUID bucketId)
			throws IOException {
		List<CachedS3Object> bucketList = s3Service.getAllObjectsByBucketId(bucketId);

		return new ResponseEntity<List<CachedS3Object>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/object/getAllByBucketName/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Object>> getAllObjectsByBucketName(@PathVariable("bucketName") String bucketName)
			throws IOException {
		List<CachedS3Object> bucketList = s3Service.getAllObjectsByBucketName(bucketName);

		return new ResponseEntity<List<CachedS3Object>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/updateByName/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<CachedS3Bucket> updateCacheByBucket(@PathVariable("bucketname") String bucketname)
			throws IOException {
		CachedS3Bucket result = s3Service.updateCacheByBucket(bucketname);

		return new ResponseEntity<CachedS3Bucket>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/updateById/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<CachedS3Bucket> updateCacheByBucket(@PathVariable("bucketId") UUID bucketId)
			throws IOException {
		CachedS3Bucket result = s3Service.updateCacheByBucket(bucketId);

		return new ResponseEntity<CachedS3Bucket>(result, HttpStatus.OK);
	}

}
