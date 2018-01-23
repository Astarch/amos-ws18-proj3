package de.pretrendr.el;

import java.util.List;

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
}
