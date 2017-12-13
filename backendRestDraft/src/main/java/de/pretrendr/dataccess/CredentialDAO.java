package de.pretrendr.dataccess;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.pretrendr.model.Credential;

public interface CredentialDAO extends ElasticsearchRepository<Credential, String> {

}
