package de.pretrendr.businesslogic;

import java.util.List;
import java.util.Map;

import de.pretrendr.dataccess.ArticleDAO;
import de.pretrendr.model.Article;

public interface ArticleService {

	ArticleDAO getArticleRepository();

	Article save(Article article);

	List<Article> save(List<Article> articles);

	void delete(Article article);

	Article findOne(String id);

	Iterable<Article> findAll();

	List<Article> findAllByTerm(String string);

	long countByTerm(String term);

	Map<String, Long> countByTermAndDay(String term);

	Map<String, Long> countByTermAndDay(String term, String from, String to);

	Map<String, Long> countByTermAndDayFromTo(String term, int yearFrom, int monthFrom, int dayFrom, int yearTo,
			int monthTo, int dayTo);

	Map<String, Long> countByTermAndMonth(String term);

	Map<String, Long> countByTermAndMonth(String term, String from, String to);

	Map<String, Long> countByTermAndMonthFromTo(String term, int yearFrom, int monthFrom, int dayFrom, int yearTo,
			int monthTo, int dayTo);

	void crawlData();

	long countAll();

	void deleteAll();

}
