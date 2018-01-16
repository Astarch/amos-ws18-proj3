package de.pretrendr.dataccess;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Article;

public interface ArticleDAO extends ElasticsearchRepository<Article, String> {

	List<Article> findAllByTitleContaining(String string);

	long countByTitleContaining(String term);

	long countByTitleContainingAndYearAndMonthAndDay(String term, String year, String month, String day);

	// @Query("{\"bool\": {\"must\": [{\"match\": {\"sqldate\": \"?0\"}}]}}")
	// Page<Article> findBySQLDateCustom(String term, Pageable pageable);

	@Override
	void deleteAll();
}
