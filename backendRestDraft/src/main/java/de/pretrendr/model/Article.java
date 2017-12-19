package de.pretrendr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "credentials", type = "credential")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	@Id
	private String url;
	private String eventDate;
	private String mentionDate;
	private String year;
	private String month;
	private String day;
	private String domain;
	private String title;
}
