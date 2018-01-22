package de.pretrendr.businesslogic;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.pretrendr.dataccess.ArticleDAO;
import de.pretrendr.dataccess.GdeltCsvCacheDAO;
import de.pretrendr.model.Article;
import de.pretrendr.model.GdeltCsvCache;
import de.pretrendr.model.QGdeltCsvCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

	@Getter
	private ArticleDAO articleRepository;
	private GdeltCsvCacheDAO gdeltCsvCacheDAO;

	@Autowired
	public ArticleServiceImpl(ArticleDAO articleRepository, GdeltCsvCacheDAO gdeltCsvCacheDAO) {
		this.articleRepository = articleRepository;
		this.gdeltCsvCacheDAO = gdeltCsvCacheDAO;
	}

	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	@Override
	public Article save(Article article) {
		return articleRepository.save(article);
	}

	@Override
	public void delete(Article article) {
		articleRepository.delete(article);
	}

	@Override
	public void delete(List<Article> articles) {
		articleRepository.delete(articles);
	}

	@Override
	public Article findOne(String id) {
		return articleRepository.findOne(id);
	}

	@Override
	public Iterable<Article> findAll() {
		return articleRepository.findAll(new PageRequest(0, 100));
	}

	@Override
	public List<Article> save(List<Article> articles) {
		return Lists.newArrayList(articleRepository.save(articles));
	}

	@Override
	public List<Article> findAllByTerm(String string) {
		return articleRepository.findAllByTitleContaining(string); // articleRepository.findAllByTitleContaining(string);
	}

	@Override
	public long countByTerm(String term) {
		return articleRepository.countByTitleContaining(term);
	}

	@Override
	public Map<String, Long> countByTermAndDay(String term) {
		Map<String, Long> map = Maps.newHashMap();
		for (int i = 2017; i < 2018; i++) {
			String year = Integer.toString(i);
			for (int m = 1; m <= 12; m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = 1; d <= 31; d++) {
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (map.containsKey(year + month)) {
						map.put(year + month + day, count + map.get(year + month));
					} else {
						map.put(year + month + day, count);
					}
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, Long> countByTermAndMonthFromTo(String term, int yearFrom, int monthFrom, int dayFrom,
			int yearTo, int monthTo, int dayTo) {
		boolean firstRun = true;
		Map<String, Long> map = Maps.newHashMap();
		for (int i = yearFrom; i <= yearTo; i++) {
			String year = Integer.toString(i);
			for (int m = firstRun ? monthFrom : 1; m <= (i == yearTo ? monthTo : 12); m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = firstRun ? dayFrom : 1; d <= (i == yearTo && m == monthTo ? dayTo : 31); d++) {
					firstRun = false;
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (map.containsKey(year + month)) {
						map.put(year + month, count + map.get(year + month));
					} else {
						map.put(year + month, count);
					}
				}
			}
		}
		return map;
	}

	@Override
	public void crawlData() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new URL("http://data.gdeltproject.org/gdeltv2/masterfilelist.txt").openStream()))) {
			File tmpFile = new File("gdelt" + File.separator + "master.txt");
			new File(tmpFile.getParent()).mkdirs();

			FileOutputStream fos = new FileOutputStream(tmpFile);
			BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(fos));

			String line;
			while ((line = br.readLine()) != null) {
				bos.write(line);
				bos.write(System.lineSeparator());
			}

			fos.close();
		} catch (MalformedURLException e1) {
			log.error("Malformed URL", e1);
		} catch (IOException e1) {
			log.error("ioex", e1);
		}

		File masterFile = new File("gdelt" + File.separator + "master.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(masterFile))) {
			byte[] buffer = new byte[1024];
			String line;
			String id;
			String url;
			String eventDate;
			String mentionDate;
			int year = 0;
			int month = 0;
			int day = 0;
			String domain;
			String title;
			int outerArticleCount = 0;
			int fileCount = 0;
			int skipped = 0;
			int masterLineCount = 0;
			int articleLimit = 10000000;
			int fileLimit = 10000;
			// read masterfile line by line
			long startTime = System.nanoTime();
			Pattern pattern = Pattern.compile("([0-9]*?) ([0-9a-f]*) http://data.gdeltproject.org/gdeltv2/(.*)");
			Pattern innerPattern = Pattern.compile("([0-9]*)\\t([0-9]*)\\t([0-9]*)\\t([0-9])\\t(.*?)\\t(http\\S*)");
			int skipOldEntries = 0;
			while ((line = br.readLine()) != null) {
				Matcher innerMatcher = pattern.matcher(line);
				skipOldEntries++;
				if (innerMatcher.find()) {
					if (innerMatcher.group(3).startsWith("2017")) {
						log.info("skipped " + skipOldEntries + " which were from prior 2017. now reading file from: "
								+ line);
						break;
					}
				}
			}

			while ((line = br.readLine()) != null && outerArticleCount < articleLimit && fileCount < fileLimit) {
				masterLineCount++;
				if (masterLineCount % 5 != 0) { // read only each n-th file, for better data distribution over days
					continue;
				}
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String zipUrl = matcher.group(3);
					if (!zipUrl.startsWith("2017")) {
						break;
					}
					if (zipUrl.contains("mention")) { // line matches pattern and contains "mention" (other
														// lines/zipfiles will be ignored)
						fileCount++;
						if (!gdeltCsvCacheDAO.findAll(QGdeltCsvCache.gdeltCsvCache.zipUrl.eq(zipUrl)).iterator()
								.hasNext()) {
							if (skipped > 0) {
								log.info("skipped " + skipped + " which were already cached.");
								skipped = 0;
							}
							try {
								ZipInputStream zipInput = new ZipInputStream(
										new URL("http://data.gdeltproject.org/gdeltv2/" + zipUrl).openStream());
								ZipEntry ze = zipInput.getNextEntry();
								int zipCount = 0;
								while (ze != null) { // read all files in zipfile
									String fileName = ze.getName();
									File tmpFile = new File("gdelt" + File.separator + fileName.substring(0, 4)
											+ File.separator + fileName.substring(4, 6) + File.separator
											+ fileName.substring(6, 8) + File.separator + fileName);
									new File(tmpFile.getParent()).mkdirs();

									FileOutputStream fos = new FileOutputStream(tmpFile);

									int len;
									while ((len = zipInput.read(buffer)) > 0) {
										fos.write(buffer, 0, len);
									}

									fos.close();

									BufferedReader tmpReader = new BufferedReader(new FileReader(tmpFile));
									int innerCount = 0;
									List<Article> articles = Lists.newArrayList();
									while ((line = tmpReader.readLine()) != null) { // read file in zip line by line
										innerCount++;
										zipCount++;
										outerArticleCount++;
										Matcher innerMatcher = innerPattern.matcher(line);
										if (innerMatcher.find()) {
											id = innerMatcher.group(1);
											eventDate = innerMatcher.group(2);
											mentionDate = innerMatcher.group(3);
											year = Integer.parseInt(mentionDate.substring(0, 4));
											month = Integer.parseInt(mentionDate.substring(4, 6));
											day = Integer.parseInt(mentionDate.substring(6, 8));
											domain = innerMatcher.group(5);
											url = innerMatcher.group(6);
											title = url.replaceAll("[^0-9a-zA-Z]", " ").replaceAll("[ ]+", " ");
											articles.add(
													new Article(id, url, domain, title, mentionDate, year, month, day));
										}
										if (innerCount % 10000 == 0) {
											log.debug("saving articles: " + outerArticleCount + "(+" + articles.size()
													+ ") from " + fileCount + " files @ "
													+ Math.floor(outerArticleCount
															/ ((System.nanoTime() - startTime) / 1e9) * 100) / 100
													+ " last: " + year + month + day);
											save(articles);
											articles.clear();
										}
									}
									tmpReader.close();
									if (articles.size() > 0) {
										log.debug("saving articles: " + outerArticleCount + "(+" + articles.size()
												+ ") from " + fileCount + " files @ " + Math.floor(outerArticleCount
														/ ((System.nanoTime() - startTime) / 1e9) * 100) / 100
												+ " last: " + year + month + day);
										save(articles);
									}
									articles.clear();

									ze = zipInput.getNextEntry();
								}
								gdeltCsvCacheDAO.save(new GdeltCsvCache(zipUrl, zipCount));
							} catch (FileNotFoundException e) {
								log.error("404 error: " + line);
							}
						} else {
							skipped++;
						}
					}
				} else {
					log.error("parse error:" + line);
				}
			}
		} catch (MalformedURLException e1) {
			log.error("Malformed URL", e1);
		} catch (IOException e1) {
			log.error("ioex", e1);
		}
	}

	@Override
	public Map<String, Long> countByTermAndDay(String term, String from, String to) {
		Map<String, Long> resultMap = Maps.newHashMap();
		// NativeSearchQueryBuilder searchBuilder = new
		// NativeSearchQueryBuilder().withQuery(QueryBuilder)(query).addIndex("jug").addType("talk");
		// SearchResponse result =
		// elasticsearchOperations.getClient().execute(searchBuilder.build());

		// TermQueryBuilder aggregation = AggregationBuilders.filter("term",
		// ).field("tags")
		// .order(Terms.Order.aggregation("_count", false));
		// SearchResponse response =
		// client.prepareSearch("blog").setTypes("article").addAggregation(aggregation).execute()
		// .actionGet();
		//
		// Map<String, Aggregation> results = response.getAggregations().asMap();
		// StringTerms topTags = (StringTerms) results.get("top_tags");
		//
		// List<String> keys = topTags.getBuckets().stream().map(b ->
		// b.getKeyAsString()).collect(toList());

		// @// @formatter:off
		String query = "{" + 
				"	\"size\": 1," + 
				"	\"aggs\": {" + 
				"		\"main\": {" + 
				"			\"filter\" : { \"regexp\": { \"title\": \".*bitcoin.*\" } }," + 
				"			\"aggs\": {" + 
				"				\"year\": {" + 
				"					\"terms\": {" + 
				"						\"field\": \"year.raw\"" + 
				"					}," + 
				"					\"aggs\": {" + 
				"						\"month\": {" + 
				"							\"terms\": {" + 
				"								\"order\" : { \"_term\" : \"asc\" }," + 
				"								\"field\": \"month.raw\"" + 
				"							}," + 
				"							\"aggs\": {" + 
				"								\"day\": {" + 
				"									\"terms\": {" + 
				"										\"order\" : { \"_term\" : \"asc\" }," + 
				"										\"field\": \"day.raw\"," + 
				"										\"size\": 31" + 
				"									}" + 
				"								}" + 
				"							}          " + 
				"						}" + 
				"					}" + 
				"				}" + 
				"			}" + 
				"		}" + 
				"	}" + 
				"}";
		// @formatter:on

		SearchQuery aSearchQuery = new NativeSearchQueryBuilder()
				.withQuery(boolQuery().must(termQuery("title", term))
						.must(rangeQuery("dateadded").from(from + "000000").to(to + "235959")))
				.withIndices("article-2018.01.18").withTypes("csv")
				.addAggregation(
						terms("byYear").field("year").size(10)
								.subAggregation(AggregationBuilders.terms("byMonth").field("month").size(12)
										.subAggregation(AggregationBuilders.terms("byDay").field("day")).size(31)))
				.build();
		Aggregations aField1Aggregations = elasticsearchOperations.query(aSearchQuery,
				new ResultsExtractor<Aggregations>() {
					@Override
					public Aggregations extract(SearchResponse aResponse) {
						return aResponse.getAggregations();
					}
				});
		Terms aField1Terms = aField1Aggregations.get("byYear");
		aField1Terms.getBuckets().stream().forEach(yearBucket -> {
			Object yearValue = yearBucket.getKey();
			Terms aField2Terms = yearBucket.getAggregations().get("byMonth");

			aField2Terms.getBuckets().stream().forEach(monthBucket -> {
				Object monthValue = monthBucket.getKey();
				Terms aField3Terms = monthBucket.getAggregations().get("byDay");

				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Object dayValue = dayBucket.getKey();
					Long count = dayBucket.getDocCount();

					String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
					String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString()
							: monthValue.toString();
					String day = dayValue.toString().length() < 2 ? "0" + dayValue.toString() : dayValue.toString();
					resultMap.put(year + month + day, count);
				});
			});
		});

		return resultMap;
	}

	@Override
	public long countAll() {
		return articleRepository.count();
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public Map<String, Long> countByTermAndMonth(String term, String from, String to) {
		if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
			try {
				int yearFrom = Integer.parseInt(from.substring(0, 4));
				int monthFrom = Integer.parseInt(from.substring(4, 6));
				int dayFrom = Integer.parseInt(from.substring(6, 8));
				int yearTo = Integer.parseInt(to.substring(0, 4));
				int monthTo = Integer.parseInt(to.substring(4, 6));
				int dayTo = Integer.parseInt(to.substring(6, 8));
				return countByTermAndMonthFromTo(term, yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo);
			} catch (NumberFormatException e) {
				return Maps.newHashMap();
			}
		} else {
			return countByTermAndMonth(term);
		}
	}

	@Override
	public Map<String, Long> countByTermAndMonth(String term) {
		Map<String, Long> map = Maps.newHashMap();
		for (int i = 2017; i < 2018; i++) {
			String year = Integer.toString(i);
			for (int m = 1; m <= 12; m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = 1; d <= 31; d++) {
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (map.containsKey(year + month)) {
						map.put(year + month, count + map.get(year + month));
					} else {
						map.put(year + month, count);
					}
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, Long> countByTermAndDayFromTo(String term, int yearFrom, int monthFrom, int dayFrom, int yearTo,
			int monthTo, int dayTo) {
		boolean firstRun = true;
		Map<String, Long> map = Maps.newHashMap();
		for (int i = yearFrom; i <= yearTo; i++) {
			String year = Integer.toString(i);
			for (int m = firstRun ? monthFrom : 1; m <= (i == yearTo ? monthTo : 12); m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = firstRun ? dayFrom : 1; d <= (i == yearTo && m == monthTo ? dayTo : 31); d++) {
					firstRun = false;
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (map.containsKey(year + month)) {
						map.put(year + month + day, count + map.get(year + month));
					} else {
						map.put(year + month + day, count);
					}
				}
			}
		}
		return map;
	}
}