package de.pretrendr.model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;

import de.pretrendr.util.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CachedS3Bucket")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "objects", "wordCount" })
@EqualsAndHashCode(exclude = { "objects", "wordCount" })
public class CachedS3Bucket {
	public CachedS3Bucket(String name) {
		this.name = name;
		this.created = DateTime.now();
		this.lastModified = DateTime.now();
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(length = 16)
	private UUID id;

	@Column
	private String name;

	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime created;

	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateSerializer.class)
	private DateTime lastModified;

	@JsonIgnore
	@OneToMany(mappedBy = "bucket", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	Set<CachedS3Object> objects = Sets.newHashSet();

	@JsonIgnore
	@OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<CachedS3WordCountPair> wordCount = Sets.newHashSet();;
}
