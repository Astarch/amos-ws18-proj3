package de.pretrendr.controller;

import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import de.pretrendr.businesslogic.ArticleService;
import de.pretrendr.dataccess.GdeltCsvCacheDAO;
import de.pretrendr.model.Article;
import de.pretrendr.model.GdeltCsvCache;

/**
 * Dummy Controller to provide simple Request-Response scenarios without the
 * need to login first.
 * 
 * @author Tristan Schneider
 *
 */
@RequestMapping("/api/dummy")
@RestController
@CrossOrigin
public class DummyController {
	@Autowired
	ArticleService articleService;

	@Autowired
	GdeltCsvCacheDAO gdeltCsvCacheDAO;

	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	@RequestMapping(value = "/wordcount/{text}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByText(@PathVariable String text) {
		Map<String, Long> content = Maps.newHashMap();

		content.put("*", articleService.countAll());

		for (String s : text.split(" ")) {
			long count = articleService.countByTerm(s);
			if (count > 0) {
				content.put(s, count);
			}
		}
		return new ResponseEntity<Map<String, Long>>(content, HttpStatus.OK);
	}

	@RequestMapping(value = "/cache", method = RequestMethod.GET)
	public ResponseEntity<Page<GdeltCsvCache>> getCache(
			@RequestParam(value = "number", defaultValue = "0") final int number,
			@RequestParam(value = "size", defaultValue = "100") final int size) throws IOException {
		Page<GdeltCsvCache> list = gdeltCsvCacheDAO.findAll(new PageRequest(number, size));

		return new ResponseEntity<Page<GdeltCsvCache>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordcountByDay/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByDay(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to) throws IOException {
		Map<String, Long> list = articleService.countByTermAndDay(term, from, to);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to) throws IOException {
		Map<String, Long> list = articleService.countByTermAndMonth(term, from, to);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/findall", method = RequestMethod.GET)
	public ResponseEntity<List<Article>> getAll() throws IOException {
		List<Article> list = Lists.newArrayList(articleService.findAll());

		return new ResponseEntity<List<Article>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByTerm/{term}", method = RequestMethod.GET)
	public ResponseEntity<List<Article>> findByTerm(@PathVariable String term) throws IOException {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(regexpQuery("title", ".*" + term + ".*"))
				.build();
		List<Article> articles = elasticsearchOperations.queryForList(searchQuery, Article.class);
		return new ResponseEntity<List<Article>>(articles, HttpStatus.OK);
	}
}
