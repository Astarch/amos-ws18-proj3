package de.pretrendr.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegUser {
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private String address;
	private String phone;
}
