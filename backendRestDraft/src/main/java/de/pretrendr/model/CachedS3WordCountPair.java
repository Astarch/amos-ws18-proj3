package de.pretrendr.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import de.pretrendr.model.CachedS3WordCountPair.CachedS3WordCountPairId;
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
@IdClass(CachedS3WordCountPairId.class)
public class CachedS3WordCountPair {
	@Data
	public static class CachedS3WordCountPairId implements Serializable {
		private static final long serialVersionUID = 1L;
		private UUID bucket;
		private String word;
	}

	@Id
	@JoinColumn
	private CachedS3Bucket bucket;

	@Id
	@Column
	private String word;

	@Column
	private int count;
}
