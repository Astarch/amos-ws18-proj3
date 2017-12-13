package de.pretrendr.businesslogic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.pretrendr.model.Credential;

import java.util.List;

public interface ArticleService {

	Credential save(Credential book);

	void delete(Credential book);

	Credential findOne(String id);

	Iterable<Credential> findAll();

	Page<Credential> findByAuthor(String author, PageRequest pageRequest);

	List<Credential> findByTitle(String title);

}
