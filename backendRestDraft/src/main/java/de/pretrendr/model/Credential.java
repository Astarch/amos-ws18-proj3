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
public class Credential {
	@Id
	private String email;
	private String username;
	private String password;
	private String platform;
}
