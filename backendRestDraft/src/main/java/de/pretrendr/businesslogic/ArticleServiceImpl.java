package de.pretrendr.businesslogic;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
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
import de.pretrendr.model.enums.SearchMethod;
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
	public List<Article> findAllByTerm(String term) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(regexpQuery("title", toRegEx(term)))
				.build();
		List<Article> articles = elasticsearchOperations.queryForList(searchQuery, Article.class);
		return articles;
	}

	@Override
	public long countByTerm(String term) {
		return articleRepository.countByTitleContaining(term);
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
			int articleLimit = 1000000;
			int fileLimit = 10000;
			long startTime = System.nanoTime();
			Pattern pattern = Pattern.compile("([0-9]*?) ([0-9a-f]*) http://data.gdeltproject.org/gdeltv2/(.*)");
			Pattern innerPattern = Pattern.compile("([0-9]*)\\t([0-9]*)\\t([0-9]*)\\t([0-9])\\t(.*?)\\t(http\\S*)");
			int skipOldEntries = 0;
			// read masterfile line by line
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
	public long countAll() {
		return articleRepository.count();
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public Map<String, Long> countByTermAndDay(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	private String toRegEx(String term) {
		String regEx = ".*(";
		regEx += Arrays.stream(term.toLowerCase().split(" ")).collect(Collectors.joining("|"));
		regEx += ").*";
		return regEx;

	}

	private QueryBuilder buildSearchQuery(String term, String from, String to, SearchMethod method) {
		BoolQueryBuilder boolQueryBuilder = boolQuery();
		String cleanTerm = term.toLowerCase().replaceAll("[^a-z0-9 -_#]", "");
		String[] terms = cleanTerm.split(" ");
		String regEx;
		switch (method) {
		default:
		case ALL:
			for (String t : terms) {
				boolQueryBuilder.must(regexpQuery("title", ".*" + t + ".*"));
			}

			boolQueryBuilder.must(rangeQuery("dateadded").from(from + "000000").to(to + "235959"));
			break;
		case ANY:
			regEx = ".*(";
			regEx += Arrays.stream(terms).collect(Collectors.joining("|"));
			regEx += ").*";

			boolQueryBuilder.must(regexpQuery("title", regEx));
			boolQueryBuilder.must(rangeQuery("dateadded").from(from + "000000").to(to + "235959"));
			break;
		case EXACT:
			regEx = ".*(";
			regEx += Arrays.stream(terms).collect(Collectors.joining(").*("));
			regEx += ").*";

			boolQueryBuilder.must(regexpQuery("title", regEx));
			boolQueryBuilder.must(rangeQuery("dateadded").from(from + "000000").to(to + "235959"));
			break;
		case RAW:
			boolQueryBuilder.must(regexpQuery("title", term));
			break;
		}
		return boolQueryBuilder;
	}

	@Override
	public Map<String, Long> countByTermAndMonth(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByMonth(term, from, to, method);
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
				Long count = monthBucket.getDocCount();

				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				resultMap.put(year + month, count);
			});
		});
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	private SearchQuery buildAggregationByMonth(String term, String from, String to, SearchMethod method) {
		// @formatter:off
		return new NativeSearchQueryBuilder()
				.withQuery(
					buildSearchQuery(term, from, to, method)
				)
				.withIndices("article-2018.01.18")
				.withTypes("csv")
				.addAggregation(
					terms("byYear")
					.field("year")
					.size(10)
					.order(Order.term(true))
					.subAggregation(
						terms("byMonth")
						.field("month")
						.size(10)
						.order(Order.term(true))
					)
				)
				.build();
		// @formatter:on
	}

	private SearchQuery buildAggregationByDay(String term, String from, String to, SearchMethod method) {
		// @formatter:off
		return new NativeSearchQueryBuilder()
				.withQuery(
					buildSearchQuery(term, from, to, method)
				)
				.withIndices("article-2018.01.18")
				.withTypes("csv")
				.addAggregation(
					terms("byYear")
					.field("year")
					.size(10)
					.order(Order.term(true))
					.subAggregation(
						terms("byMonth")
						.field("month")
						.size(12)
						.order(Order.term(true))
						.subAggregation(
							terms("byDay")
							.field("day")
							.size(31)
							.order(Order.term(true))
						)
					)
				)
				.build();
		// @formatter:on
	}

	@Override
	public Map<String, Long> averageCountByTermAndMonth(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
				final long[] values = { 0L, 0L };
				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Long count = dayBucket.getDocCount();
					values[0] += count;
					values[1]++;
				});
				long avg = values[0] / values[1];
				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				resultMap.put(year + month, avg);
			});
		});
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	@Override
	public Map<String, Long> minCountByTermAndMonth(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
				final long[] values = { -1L };
				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Long count = dayBucket.getDocCount();
					if (values[0] < 0) {
						values[0] = count;
					} else {
						values[0] = Math.min(values[0], count);
					}
				});
				long min = values[0];
				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				resultMap.put(year + month, min);
			});
		});
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	@Override
	public Map<String, Long> maxCountByTermAndMonth(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
				final long[] values = { -1L };
				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Long count = dayBucket.getDocCount();
					if (values[0] < 0) {
						values[0] = count;
					} else {
						values[0] = Math.max(values[0], count);
					}
				});
				long min = values[0];
				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				resultMap.put(year + month, min);
			});
		});
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	@Override
	public Map<String, Long> medCountByTermAndMonth(String term, String from, String to, SearchMethod method,
			boolean normalize) {
		Map<String, Long> resultMap = Maps.newHashMap();
		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
				final List<Long> list = Lists.newArrayList();
				long median;
				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Long count = dayBucket.getDocCount();
					list.add(count);
				});
				Collections.sort(list);
				if (list.size() % 2 == 0)
					median = (list.get(list.size() / 2) + list.get(list.size() / 2 - 1)) / 2;
				else
					median = list.get(list.size() / 2);
				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				resultMap.put(year + month, median);
			});
		});
		if (normalize)
			normalizeMap(resultMap);

		return resultMap;
	}

	@Override
	public void normalizeMap(Map<String, Long> list) {
		Long maxEntry = null;
		for (Map.Entry<String, Long> entry : list.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry) > 0) {
				maxEntry = entry.getValue();
			}
		}
		if (maxEntry != null && maxEntry > 0) {
			double normalizeFactor = 1d / (maxEntry / 100d);
			for (Map.Entry<String, Long> entry : list.entrySet()) {
				list.put(entry.getKey(), (long) (entry.getValue() * normalizeFactor));
			}
		}
	}

	@Override
	public Map<String, Map<String, Long>> metaDataByTermAndMonth(String term, String from, String to,
			SearchMethod method) {
		Map<String, Map<String, Long>> resultMap = Maps.newHashMap();
		Map<String, Long> countMap = Maps.newHashMap();
		Map<String, Long> minMap = Maps.newHashMap();
		Map<String, Long> maxMap = Maps.newHashMap();
		Map<String, Long> avgMap = Maps.newHashMap();
		Map<String, Long> medMap = Maps.newHashMap();

		SearchQuery aSearchQuery = buildAggregationByDay(term, from, to, method);
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
				final long[] values = { -1L, -1L, 0L };
				final List<Long> list = Lists.newArrayList();
				long median;

				aField3Terms.getBuckets().stream().forEach(dayBucket -> {
					Object dayValue = dayBucket.getKey();
					Long count = dayBucket.getDocCount();

					values[0] = values[0] == -1L ? count : Math.min(count, values[0]);
					values[1] = Math.max(count, values[1]);
					values[2] += count;
					list.add(count);
				});

				String year = yearValue.toString().length() < 2 ? "0" + yearValue.toString() : yearValue.toString();
				String month = monthValue.toString().length() < 2 ? "0" + monthValue.toString() : monthValue.toString();
				Collections.sort(list);
				if (list.size() % 2 == 0)
					median = (list.get(list.size() / 2) + list.get(list.size() / 2 - 1)) / 2;
				else
					median = list.get(list.size() / 2);

				minMap.put(year + month, values[0]);
				maxMap.put(year + month, values[1]);
				avgMap.put(year + month, values[2] / list.size());
				medMap.put(year + month, median);
				countMap.put(year + month, values[2]);
			});
		});

		resultMap.put("count", countMap);
		resultMap.put("min", minMap);
		resultMap.put("max", maxMap);
		resultMap.put("avg", avgMap);
		resultMap.put("med", medMap);

		return resultMap;
	}
}