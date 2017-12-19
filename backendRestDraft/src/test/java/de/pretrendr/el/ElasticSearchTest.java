package de.pretrendr.el;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.assertj.core.util.Lists;
import org.junit.Test;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.model.Article;

public class ElasticSearchTest extends PretrendrTestBase {

	// @Test
	public void test1() throws Exception {
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
			while ((line = br.readLine()) != null && outerArticleCount < 1000000) {
				Pattern pattern = Pattern.compile("([0-9]*?) ([0-9a-f]*) http://data.gdeltproject.org/gdeltv2/(.*)");
				Matcher matcher = pattern.matcher(line);
				String zipUrl;
				if (matcher.find()) {
					if (matcher.group(3).contains("mention")) {
						try {

							zipUrl = matcher.group(3);
							System.out.println(zipUrl);
							ZipInputStream zipInput = new ZipInputStream(
									new URL("http://data.gdeltproject.org/gdeltv2/" + zipUrl).openStream());
							ZipEntry ze = zipInput.getNextEntry();
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
								int innerCount = 10000;
								List<Article> articles = Lists.newArrayList();
								while ((line = tmpReader.readLine()) != null && outerArticleCount < 1000000) {
									innerCount++;
									outerArticleCount++;
									Pattern innerPattern = Pattern
											.compile("([0-9]*)\\t([0-9]*)\\t([0-9]*)\\t([0-9])\\t(.*?)\\t(http\\S*)");
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
										articles.add(new Article(url, eventDate, mentionDate, year, month, day, domain,
												title));
									}
									if (innerCount % 10000 == 0) {
										System.out.println(outerArticleCount + "(+" + articles.size() + ")");
										save(articles);
									}
								}
								tmpReader.close();
								save(articles);
								articles.clear();

								ze = zipInput.getNextEntry();
							}
						} catch (FileNotFoundException e) {
							System.err.println("404 error: " + line);
						}
					}
				} else {
					System.err.println("parse error:" + line);
				}
			}
		}
	}

	private void save(List<Article> articles) {
		if (!articles.isEmpty()) {
			articleService.save(articles);
			articles.clear();
		}
	}

	@Test
	public void test2() {
		List<Article> asd = Lists.newArrayList(articleService.findAllByTerm("bitcoin"));
		for (Article a : asd) {
			System.out.println(a);
		}
	}

	@Test
	public void test3() {
		System.out.println(articleService.countByTerm("bitcoin"));
	}

	@Test
	public void test4() {
		System.out.println(articleService.countByTermAndDay("bitcoin"));
	}
}
