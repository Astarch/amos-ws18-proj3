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
