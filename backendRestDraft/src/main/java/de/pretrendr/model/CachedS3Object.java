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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.pretrendr.util.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CachedS3Object")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "bucket" })
@EqualsAndHashCode(exclude = { "bucket" })
public class CachedS3Object {
	public CachedS3Object(CachedS3Bucket bucket, String name) {
		this.bucket = bucket;
		this.name = name;
	}

	public CachedS3Object(CachedS3Bucket bucket, String name, DateTime created, DateTime lastModified) {
		this.created = created;
		this.lastModified = lastModified;
		this.bucket = bucket;
		this.name = name;
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
	private String name;

	@Column(updatable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime created;

	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime lastModified;
}