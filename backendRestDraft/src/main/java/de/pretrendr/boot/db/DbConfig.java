package de.pretrendr.boot.db;

import javax.sql.DataSource;

import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.pretrendr.model.Article;

/**
 * Database configuration.
 * 
 * @author Tristan Schneider
 *
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = { "de.pretrendr" })
@EnableJpaRepositories(basePackages = { "de.pretrendr" })
@EnableTransactionManagement
@EnableElasticsearchRepositories(basePackages = { "de.pretrendr" })
public class DbConfig {
	/**
	 * {@link DataSource} based on configuration file. See
	 * <i>usermanagement.datasource</i> in <b>application.properties</b> for more
	 * details.
	 * 
	 * @return {@link DataSource} used for JPA persistence layer.
	 */
	@Bean
	@ConfigurationProperties("usermanagement.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public NodeBuilder nodeBuilder() {
		return new NodeBuilder();
	}

	// Embedded Elasticsearch Server
	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		ElasticsearchTemplate et = new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
		et.putMapping(Article.class);
		return et;
	}

}
