package de.pretrendr.model.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "customindex", type = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	
    @Id
    private String id;
    private String title;
    private String author;
    private String releaseDate;
}
