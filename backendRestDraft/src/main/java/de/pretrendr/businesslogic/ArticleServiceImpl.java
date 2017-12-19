package de.pretrendr.businesslogic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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

	@Override
	public List<Article> findAllByTerm(String string) {
		return articleRepository.findAllByTitleContaining(string);
	}

	@Override
	public long countByTerm(String term) {
		return articleRepository.countByTitleContaining(term);
	}

	@Override
	public Map<String, Long> countByTermAndDay(String term) {
		Map<String, Long> map = Maps.newHashMap();
		for (int i = 2015; i < 2016; i++) {
			String year = Integer.toString(i);
			for (int m = 2; m <= 3; m++) {
				String month = (m < 10 ? "0" : "") + Integer.toString(m);
				for (int d = 1; d <= 31; d++) {
					String day = (d < 10 ? "0" : "") + Integer.toString(d);
					long count = articleRepository.countByTitleContainingAndYearAndMonthAndDay(term, year, month, day);
					if (count > 0) {
						map.put(year + month + day, count);
					}
				}
			}
		}
		return map;
	}
}