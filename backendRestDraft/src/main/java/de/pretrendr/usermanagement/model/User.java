package de.pretrendr.usermanagement.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "User")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private String address;
	private String phone;
}