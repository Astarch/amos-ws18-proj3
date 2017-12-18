package de.pretrendr.dataccess;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Article;

public interface ArticleDAO extends ElasticsearchRepository<Article, String> {

}
