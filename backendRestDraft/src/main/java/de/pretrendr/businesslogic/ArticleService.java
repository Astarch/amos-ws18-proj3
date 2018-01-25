package de.pretrendr.businesslogic;

import java.util.List;
import java.util.Map;

import de.pretrendr.dataccess.ArticleDAO;
import de.pretrendr.model.Article;
import de.pretrendr.model.enums.SearchMethod;

public interface ArticleService {

	ArticleDAO getArticleRepository();

	Article save(Article article);

	List<Article> save(List<Article> articles);

	void delete(Article article);

	Article findOne(String id);

	Iterable<Article> findAll();

	List<Article> findAllByTerm(String string);

	long countByTerm(String term);

	Map<String, Long> countByTermAndDay(String term, String from, String to, SearchMethod method);

	Map<String, Long> countByTermAndMonth(String term, String from, String to, SearchMethod method);

	Map<String, Long> averageCountByTermAndMonth(String term, String from, String to, SearchMethod method);

	Map<String, Long> minCountByTermAndMonth(String term, String from, String to, SearchMethod method);

	Map<String, Long> maxCountByTermAndMonth(String term, String from, String to, SearchMethod method);

	Map<String, Long> medCountByTermAndMonth(String term, String from, String to, SearchMethod method);

	void crawlData();

	long countAll();

	void deleteAll();

	void delete(List<Article> articles);

}
