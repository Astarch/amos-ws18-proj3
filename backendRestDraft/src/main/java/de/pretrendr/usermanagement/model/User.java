package de.pretrendr.usermanagement.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "User")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	public User(String username, String password, String firstname, String lastname, String email, String address,
			String phone) {
		this.id = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}

	@Id
	private UUID id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private String address;
	private String phone;
}