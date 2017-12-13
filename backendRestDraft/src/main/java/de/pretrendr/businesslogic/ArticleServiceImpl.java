package de.pretrendr.businesslogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.pretrendr.dataccess.CredentialDAO;
import de.pretrendr.model.Credential;

@Service
public class ArticleServiceImpl implements ArticleService {

	private CredentialDAO articleRepository;

	@Autowired
	public ArticleServiceImpl(CredentialDAO articleRepository) {
		this.articleRepository = articleRepository;
	}

	@Override
	public Credential save(Credential article) {
		return articleRepository.save(article);
	}

	@Override
	public void delete(Credential article) {
		articleRepository.delete(article);
	}

	@Override
	public Credential findOne(String id) {
		return articleRepository.findOne(id);
	}

	@Override
	public Iterable<Credential> findAll() {
		return articleRepository.findAll();
	}

	@Override
	public Page<Credential> findByAuthor(String author, PageRequest pageRequest) {
		// return articleRepository.findByAuthor(author, pageRequest);
		return null;
	}

	@Override
	public List<Credential> findByTitle(String title) {
		// return articleRepository.findByTitle(title);
		return null;
	}

}