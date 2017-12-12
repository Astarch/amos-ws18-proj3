package de.pretrendr.el;

import org.junit.Test;

import de.pretrendr.PretrendrTestBase;

public class ElasticSearchTest extends PretrendrTestBase {

	@Test
	public void test1() throws Exception {
		articleService.findAll();
	}
}
