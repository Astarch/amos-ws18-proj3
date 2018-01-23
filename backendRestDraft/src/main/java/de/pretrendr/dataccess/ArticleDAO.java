package de.pretrendr.dataccess;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Article;

public interface ArticleDAO extends ElasticsearchRepository<Article, String> {

	List<Article> findAllByTitleContaining(String string);

	long countByTitleContaining(String term);

	long countByTitleContainingAndYearAndMonthAndDay(String term, String year, String month, String day);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"type\": \"csv\"}}]}}")
	Page<Article> findByTypeCustom(Pageable pageable);

	@Override
	void deleteAll();

	void deleteByYearAndMonthAndDay(String year, String month, String day);

	List<Article> findAllByGlobaleventidContaining(String string);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"type\": \"csv\"}}]}}")
	void deleteAllByGlobaleventidContaining(String string);

	List<Article> findAllByYear(String term, Pageable page);

	void deleteAllByYear(String csv);

	void deleteAllByYearAndMonthAndDay(String year, String month, String day);

	void deleteAllByYearAndMonth(String year, String month);
}
