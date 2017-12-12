package de.pretrendr.businesslogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.pretrendr.dataccess.ArticleRepository;
import de.pretrendr.model.es.Article;

@Service
public class ArticleServiceImpl implements ArticleService {

	private ArticleRepository articleRepository;

	@Autowired
	public ArticleServiceImpl(ArticleRepository articleRepository) {
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
	public Page<Article> findByAuthor(String author, PageRequest pageRequest) {
		// return articleRepository.findByAuthor(author, pageRequest);
		return null;
	}

	@Override
	public List<Article> findByTitle(String title) {
		// return articleRepository.findByTitle(title);
		return null;
	}

}