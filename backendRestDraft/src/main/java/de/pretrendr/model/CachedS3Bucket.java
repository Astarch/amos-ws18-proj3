package de.pretrendr.model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

	@OneToMany(mappedBy = "bucket")
	Set<CachedS3Object> objects;

	@OneToMany(mappedBy = "bucket")
	Set<CachedS3WordCountPair> wordCount;
}
