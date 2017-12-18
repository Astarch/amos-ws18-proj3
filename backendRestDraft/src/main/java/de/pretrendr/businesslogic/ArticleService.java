package de.pretrendr.businesslogic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.pretrendr.model.Article;

import java.util.List;

public interface ArticleService {

	Article save(Article article);

	List<Article> save(List<Article> articles);

	void delete(Article article);

	Article findOne(String id);

	Iterable<Article> findAll();
}
