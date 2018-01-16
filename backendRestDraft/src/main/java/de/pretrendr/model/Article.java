package de.pretrendr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "logstash-2018.01.16", type = "csv")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

	@Id
	private String globaleventid;
	private String sourceurl;
	private String domain;
	private String title;
	private String dateadded;
	private String year;
	private String month;
	private String day;
}
