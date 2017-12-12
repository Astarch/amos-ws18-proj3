package de.pretrendr.businesslogic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.pretrendr.model.es.Article;

import java.util.List;

public interface ArticleService {

	Article save(Article book);

	void delete(Article book);

	Article findOne(String id);

	Iterable<Article> findAll();

	Page<Article> findByAuthor(String author, PageRequest pageRequest);

	List<Article> findByTitle(String title);

}
