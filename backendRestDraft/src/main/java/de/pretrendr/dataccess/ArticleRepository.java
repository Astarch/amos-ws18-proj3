package de.pretrendr.dataccess;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.es.Article;

public interface ArticleRepository extends CrudRepository<Article, String>, ElasticsearchRepository<Article, String> {

}
