package de.pretrendr.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

public class UserControllerTest extends PretrendrTestBase {

	private User user;
	private List<Role> rolesList;

	@Before
	public void setup() throws Exception {
		this.user = userDAO.save(new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", null));
		this.rolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "USER")));
		this.rolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "ADMIN")));
	}

	@Test
	public void getUser_validId_checkJSON() throws Exception {
		mockMvc.perform(get("/api/user/get/" + this.user.getId())).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.user.getId().toString())))
				.andExpect(jsonPath("$.username", is(this.user.getUsername())))
				.andExpect(jsonPath("$.firstname", is(this.user.getFirstname())))
				.andExpect(jsonPath("$.lastname", is(this.user.getLastname())))
				.andExpect(jsonPath("$.email", is(this.user.getEmail())))
				.andExpect(jsonPath("$.address", is(this.user.getAddress())))
				.andExpect(jsonPath("$.phone", is(this.user.getPhone())))
				.andExpect(jsonPath("$.enabled", is(this.user.isEnabled())))
				.andExpect(jsonPath("$.password").doesNotExist());
		// @formatter:on
	}

	@Test
	public void getUser_invalidId_404NOTFOUND() throws Exception {
		mockMvc.perform(get("/api/user/get/" + new UUID(0L, 0L))).andExpect(status().isNotFound());
	}
}