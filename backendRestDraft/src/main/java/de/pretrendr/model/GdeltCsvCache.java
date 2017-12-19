package de.pretrendr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "GDELTSCVCACHE")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GdeltCsvCache {
	@Id
	@Column(name = "zipurl")
	private String zipUrl;

	@Column(name = "count")
	private int count;
}
