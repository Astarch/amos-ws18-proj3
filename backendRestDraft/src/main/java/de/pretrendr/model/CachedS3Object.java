package de.pretrendr.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.pretrendr.model.CachedS3Object.CachedS3ObjectId;
import de.pretrendr.util.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CACHEDS3OBJECT")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "bucket" })
@EqualsAndHashCode(exclude = { "bucket" })
@IdClass(CachedS3ObjectId.class)
public class CachedS3Object {
	public CachedS3Object(CachedS3Bucket bucket, String name) {
		this.bucket = bucket;
		this.name = name;
		this.created = DateTime.now();
	}

	@Data
	public static class CachedS3ObjectId implements Serializable {
		private static final long serialVersionUID = 1L;
		private UUID bucket;
		private String name;
	}

	@Id
	@ManyToOne
	@JoinColumn(name = "bucketid")
	@JsonIgnore
	private CachedS3Bucket bucket;

	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "created", updatable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime created;

	@Column(name = "lastmodified")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime lastModified;
}