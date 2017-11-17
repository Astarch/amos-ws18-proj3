package de.pretrendr.usermanagement.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import scala.util.Random;

@RequestMapping("/api/dummy")
@RestController
@CrossOrigin
public class DummyController {

	@RequestMapping(value = "/graphData", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getDummyGraphData(
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "range", defaultValue = "100") int range) {
		Map<String, String> content = Maps.newHashMap();
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			content.put("word" + Integer.toString(i), Integer.toString(rand.nextInt(range)));
		}

		// GraphData graphData = new GraphData(content);

		return new ResponseEntity<Map<String, String>>(content, HttpStatus.OK);

	}
}
