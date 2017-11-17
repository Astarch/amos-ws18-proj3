package de.pretrendr.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.usermanagement.dataccess.RoleDAO;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

public class AuthenticationControllerTest extends PretrendrTestBase {

	private User user;

	private List<Role> RolesList = new ArrayList<>();

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private UserDAO userDAO;

	@Before
	public void setup() throws Exception {
		this.roleDAO.deleteAll();
		this.userDAO.deleteAll();

		this.user = userDAO.save(new User(UUID.randomUUID(), "username", "password", "firstname", "lastname", "email",
				"address", "phone", null));
		this.RolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "USER")));
		this.RolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "ADMIN")));
	}

	@Test
	public void register_existingUserName_302FOUND() throws Exception {
		User existingUsername = new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"otheremail", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingUsername)).contentType(contentType))
				.andExpect(status().isFound());
	}

	@Test
	public void register_existingUserEmail_302FOUND() throws Exception {
		User existingEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname", "email",
				"address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(contentType))
				.andExpect(status().isFound());
	}

	@Test
	public void register_InvalidEmail_TODO() throws Exception {
		// User existingEmail = new User(UUID.randomUUID(), "otherUsername", "password",
		// "firstname", "lastname",
		// "invalidEmail", "address", "phone", null);
		// mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(contentType))
		// .andExpect(status().isFound());
	}

	@Test
	public void register_ValidEmail_TODO() throws Exception {
		// User existingEmail = new User(UUID.randomUUID(), "otherUsername", "password",
		// "firstname", "lastname",
		// "validEmail@test.me", "address", "phone", null);
		// mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(contentType))
		// .andExpect(status().isFound());
	}

	@Test
	public void readSingleBookmark() throws Exception {
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

	@Override
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}