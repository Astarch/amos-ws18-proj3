package de.pretrendr.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CachedS3WordCountPair")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "bucket" })
@EqualsAndHashCode(exclude = { "bucket" })
public class CachedS3WordCountPair {

	public CachedS3WordCountPair(CachedS3Bucket bucket, String word, int count) {
		this.bucket = bucket;
		this.word = word;
		this.count = count;
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(length = 16)
	private UUID id;

	@ManyToOne
	@JoinColumn(insertable = true, updatable = false)
	@JsonIgnore
	private CachedS3Bucket bucket;

	@Column
	private String word;

	@Column
	private int count;
}
