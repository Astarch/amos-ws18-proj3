package de.pretrendr.dataccess;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Article;

public interface ArticleDAO extends ElasticsearchRepository<Article, String> {

	List<Article> findAllBySourceurlContaining(String string);

	long countBySourceurlContaining(String term);

	long countBySourceurlContainingAndMonthyear(String term, String string);

	// long countByTitleContainingAndYear(String term, String string);

	// Long countByTitleContainingAndYearAndMonthAndDay(String term, String year,
	// String month, String day);

}
