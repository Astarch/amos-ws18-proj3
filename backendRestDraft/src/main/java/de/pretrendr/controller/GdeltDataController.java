package de.pretrendr.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
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
import de.pretrendr.model.enums.SearchMethod;

@RequestMapping("/api/gdelt")
@RestController
@CrossOrigin
public class GdeltDataController {

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
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.countByTermAndDay(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.countByTermAndMonth(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/averageWordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getAverageWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.averageCountByTermAndMonth(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/minWordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getMinWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.minCountByTermAndMonth(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/maxWordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getMaxWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.maxCountByTermAndMonth(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/medWordcountByMonth/{term}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Long>> getMedWordCountByMonth(@PathVariable String term,
			@RequestParam(value = "from", defaultValue = "20170101") final String from,
			@RequestParam(value = "to", defaultValue = "20171231") final String to,
			@RequestParam(value = "method", defaultValue = "ALL") final SearchMethod method) throws IOException {
		Map<String, Long> list = articleService.medCountByTermAndMonth(term, from, to, method);

		return new ResponseEntity<Map<String, Long>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/findall", method = RequestMethod.GET)
	public ResponseEntity<List<Article>> getAll() throws IOException {
		List<Article> list = Lists.newArrayList(articleService.findAll());

		return new ResponseEntity<List<Article>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByTerm/{term}", method = RequestMethod.GET)
	public ResponseEntity<List<Article>> findByTerm(@PathVariable String term) throws IOException {
		List<Article> articles = articleService.findAllByTerm(term);
		return new ResponseEntity<List<Article>>(articles, HttpStatus.OK);
	}
}
