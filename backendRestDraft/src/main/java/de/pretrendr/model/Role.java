package de.pretrendr.model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "ROLE")
@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class Role {

	public Role(UUID id, String role) {
		this.id = id;
		this.role = role;
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", length = 16)
	private UUID id;

	@Column(name = "role")
	private String role;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "roleid"), inverseJoinColumns = @JoinColumn(name = "userid"))
	private Set<User> users;

	@Override
	public String toString() {
		return role;
	}
}