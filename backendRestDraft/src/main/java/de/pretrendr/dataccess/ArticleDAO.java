package de.pretrendr.dataccess;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Article;

public interface ArticleDAO extends ElasticsearchRepository<Article, String> {

	List<Article> findAllBySourceurlContaining(String string);

	long countBySourceurlContaining(String term);

	long countBySourceurlContainingAndSqldateStartsWith(String term, String string);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"sqldate\": \"?0\"}}]}}")
	Page<Article> findBySQLDateCustom(String term, Pageable pageable);
}
