package de.pretrendr.businesslogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import de.pretrendr.dataccess.ArticleDAO;
import de.pretrendr.model.Article;

@Service
public class ArticleServiceImpl implements ArticleService {

	private ArticleDAO articleRepository;

	@Autowired
	public ArticleServiceImpl(ArticleDAO articleRepository) {
		this.articleRepository = articleRepository;
	}

	@Override
	public Article save(Article article) {
		return articleRepository.save(article);
	}

	@Override
	public void delete(Article article) {
		articleRepository.delete(article);
	}

	@Override
	public Article findOne(String id) {
		return articleRepository.findOne(id);
	}

	@Override
	public Iterable<Article> findAll() {
		return articleRepository.findAll();
	}

	@Override
	public List<Article> save(List<Article> articles) {
		return Lists.newArrayList(articleRepository.save(articles));
	}
}