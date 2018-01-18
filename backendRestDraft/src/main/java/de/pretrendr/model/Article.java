package de.pretrendr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "article-2018.01.18", type = "csv")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

	@Id
	private String globaleventid;
	@Field(type = FieldType.String, index = FieldIndex.analyzed)
	private String sourceurl;
	@Field(type = FieldType.String, index = FieldIndex.analyzed)
	private String domain;
	@Field(type = FieldType.String, index = FieldIndex.analyzed)
	private String title;
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true, format = DateFormat.custom, pattern = "yyyyMMddHHmmss")
	private String dateadded;

	@Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
	private Integer year;
	@Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
	private Integer month;
	@Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
	private Integer day;
}
