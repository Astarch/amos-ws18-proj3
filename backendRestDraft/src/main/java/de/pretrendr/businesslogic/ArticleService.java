package de.pretrendr.businesslogic;

import java.util.List;
import java.util.Map;

import de.pretrendr.model.Article;

public interface ArticleService {

	Article save(Article article);

	List<Article> save(List<Article> articles);

	void delete(Article article);

	Article findOne(String id);

	Iterable<Article> findAll();

	List<Article> findAllByTerm(String string);

	long countByTerm(String term);

	Map<String, Long> countByTermAndDay(String term);

	void crawlData();
}
