package de.pretrendr.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.businesslogic.S3Service;

@RequestMapping("/api/s3")
@RestController
@CrossOrigin
public class S3Controller {
	private final String bucket_name = "amos-pretrendr-commoncrawl-sample";

	@Autowired
	S3Service s3Service;

	@RequestMapping(value = "/wordCountMap/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Integer>> getDummyGraphData(@PathVariable(name = "bucketname") String bucketname)
			throws IOException {
		Map<String, Integer> map = s3Service.getWorkdCountMapFromBucketName(bucketname);

		return new ResponseEntity<Map<String, Integer>>(map, HttpStatus.OK);
	}

}
