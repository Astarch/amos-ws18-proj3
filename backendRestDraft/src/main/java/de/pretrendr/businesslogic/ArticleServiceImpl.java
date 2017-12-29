package de.pretrendr.businesslogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.pretrendr.dataccess.ArticleDAO;
import de.pretrendr.dataccess.GdeltCsvCacheDAO;
import de.pretrendr.model.Article;
import de.pretrendr.model.GdeltCsvCache;
import de.pretrendr.model.QGdeltCsvCache;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

	private ArticleDAO articleRepository;
	private GdeltCsvCacheDAO gdeltCsvCacheDAO;

	@Autowired
	public ArticleServiceImpl(ArticleDAO articleRepository, GdeltCsvCacheDAO gdeltCsvCacheDAO) {
		this.articleRepository = articleRepository;
		this.gdeltCsvCacheDAO = gdeltCsvCacheDAO;
	}

	@Override
	public Article save(Article article) {
		return articleRepository.save(article);
	}

	@Override
	public void delete(Article article) {
		articleRepository.delete(article);
	}

	@Override
	public Article findOne(String id) {
		return articleRepository.findOne(id);
	}

	@Override
	public Iterable<Article> findAll() {
		return articleRepository.findAll();
	}

	@Override
	public List<Article> save(List<Article> articles) {
		return Lists.newArrayList(articleRepository.save(articles));
	}

	@Override
	public List<Article> findAllByTerm(String string) {
		return articleRepository.findAllByTitleContaining(string);
	}

	@Override
	public long countByTerm(String term) {
		return articleRepository.countByTitleContaining(term);
	}

	@Override
	public Map<String, Long> countByTermAndDay(String term) {
		Map<String, Long> map = Maps.newHashMap();
		for (int i = 2015; i < 2016; i++) {
			String year = Integer.toString(i);
			for (int m = 2; m <= 3; m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = 1; d <= 31; d++) {
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (count > 0) {
						map.put(year + month + day, count);
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
					if (count > 0) {
						map.put(year + month + day, count);
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
			byte[] buffer = new byte[1024];
			String line;
			String url;
			String eventDate;
			String mentionDate;
			String year;
			String month;
			String day;
			String domain;
			String title;
			int outerArticleCount = 0;
			int fileCount = 0;
			int skipped = 0;
			int masterLineCount = 0;
			while ((line = br.readLine()) != null && outerArticleCount < 1000000 && fileCount < 1000) {
				masterLineCount++;
				if (masterLineCount % 10 != 0) { // read only each 10th file, for better data distribution over several
													// days
					continue;
				}
				Pattern pattern = Pattern.compile("([0-9]*?) ([0-9a-f]*) http://data.gdeltproject.org/gdeltv2/(.*)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String zipUrl = matcher.group(3);
					if (zipUrl.contains("mention")) {
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
								while (ze != null) {
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
									while ((line = tmpReader.readLine()) != null) {
										innerCount++;
										zipCount++;
										outerArticleCount++;
										Pattern innerPattern = Pattern.compile(
												"([0-9]*)\\t([0-9]*)\\t([0-9]*)\\t([0-9])\\t(.*?)\\t(http\\S*)");
										Matcher innerMatcher = innerPattern.matcher(line);
										if (innerMatcher.find()) {
											eventDate = innerMatcher.group(2);
											mentionDate = innerMatcher.group(3);
											year = mentionDate.substring(0, 4);
											month = mentionDate.substring(4, 6);
											day = mentionDate.substring(6, 8);
											domain = innerMatcher.group(5);
											url = innerMatcher.group(6);
											title = url.replaceAll("[^0-9a-zA-Z]", " ").replaceAll("[ ]+", " ");
											articles.add(new Article(url, eventDate, mentionDate, year, month, day,
													domain, title));
										}
										if (innerCount % 10000 == 0) {
											log.debug("saving articles: " + outerArticleCount + "(+" + articles.size()
													+ ")");
											save(articles);
										}
									}
									tmpReader.close();
									if (articles.size() > 0) {
										log.debug(
												"saving articles: " + outerArticleCount + "(+" + articles.size() + ")");
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
		if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
			try {
				int yearFrom = Integer.parseInt(from.substring(0, 4));
				int monthFrom = Integer.parseInt(from.substring(4, 6));
				int dayFrom = Integer.parseInt(from.substring(6, 8));
				int yearTo = Integer.parseInt(to.substring(0, 4));
				int monthTo = Integer.parseInt(to.substring(4, 6));
				int dayTo = Integer.parseInt(to.substring(6, 8));
				return countByTermAndDayFromTo(term, yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo);
			} catch (NumberFormatException e) {
				return Maps.newHashMap();
			}
		} else {
			return countByTermAndDay(term);
		}
	}
}