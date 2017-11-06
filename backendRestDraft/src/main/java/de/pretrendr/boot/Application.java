package de.pretrendr.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "de.pretrendr" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Bean
	// public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
	// return new PretrenderSecurityConfig();
	// }

	// @Bean
	// public DataSource pGPoolingDataSource() {
	// PGPoolingDataSource source = new PGPoolingDataSource();
	// source.setDataSourceName("A Data Source");
	// source.setServerName("localhost");
	// source.setDatabaseName("protuberanz");
	// source.setUser("protuberanz");
	// source.setPassword("protuberanz");
	// source.setMaxConnections(10);
	// return source;
	// }

	// @Bean
	// public LocalSessionFactoryBean sessionFactory() {
	// Properties props = new Properties();
	// props.put("hibernate.dialect", PostgreSQL82Dialect.class.getName());
	// props.put("hibernate.format_sql", "true");
	// props.put("hibernate.show_sql", "true");
	// props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
	// props.put("hibernate.connection.url",
	// "jdbc:postgresql://localhost/protuberanz");
	// props.put("hibernate.connection.username", "protuberanz");
	// props.put("hibernate.connection.password", "protuberanz");
	//
	// LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
	// bean.setAnnotatedClasses(User.class, Image.class);
	// bean.setHibernateProperties(props);
	// return bean;
	// }
}
