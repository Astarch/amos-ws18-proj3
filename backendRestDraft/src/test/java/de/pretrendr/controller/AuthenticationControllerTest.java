package de.pretrendr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

public class AuthenticationControllerTest extends PretrendrTestBase {

	private User user;

	private List<Role> RolesList = new ArrayList<>();

	@Before
	public void setup() throws Exception {
		this.userDAO.deleteAll();
		this.roleDAO.deleteAll();

		this.user = userDAO.save(new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", null));
		this.RolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "USER")));
		this.RolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "ADMIN")));
	}

	@Test
	public void register_existingUserName_309() throws Exception {
		User existingUsername = new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"other@mail.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingUsername)).contentType(contentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_existingUserEmail_409() throws Exception {
		User existingEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(contentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_InvalidEmail_TODO() throws Exception {
		User invalidEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"invalidEmail", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(invalidEmail)).contentType(contentType))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void register_ValidEmail_TODO() throws Exception {
		User validEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"validEmail@test.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(validEmail)).contentType(contentType))
				.andExpect(status().isOk());
	}

	@Override
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}