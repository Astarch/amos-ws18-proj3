package de.pretrendr.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import de.pretrendr.businesslogic.ArticleService;

@RequestMapping("/api/dummy")
@RestController
@CrossOrigin
public class GdeltDataController {

	@Autowired
	ArticleService articleService;
	
	
	@RequestMapping(value = "/wordcount/{text}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByText(@PathVariable String text) {
		Map<String, Long> content = Maps.newHashMap();
		
		for(String s : text.split(" ")) {
			long count = articleService.countByTerm(s);
			if(count > 0 ) {
				content.put(s, count);
			}
		}
		return new ResponseEntity<Map<String, Long>>(content, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/wordcountByDay/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByDayByTerm(@PathVariable String term) {
		Map<String, Long> content = articleService.countByTermAndDay(term);
		return new ResponseEntity<Map<String, Long>>(content, HttpStatus.OK);
	}
}
