package de.pretrendr.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

public class AuthenticationControllerTest extends PretrendrTestBase {

	private User user;

	private List<Role> rolesList = new ArrayList<>();

	@Override
	@Before
	public void setupMvcContext() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Before
	public void setup() throws Exception {
		this.userDAO.deleteAll();
		this.roleDAO.deleteAll();

		this.rolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "USER")));
		this.rolesList.add(roleDAO.save(new Role(UUID.randomUUID(), "ADMIN")));
		this.user = userDAO.save(new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", Sets.newHashSet(rolesList)));
	}

	@Test
	public void register_existingUserName_309() throws Exception {
		User existingUsername = new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"other@mail.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingUsername)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_existingUserEmail_409() throws Exception {
		User existingEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_InvalidEmail_406() throws Exception {
		User invalidEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"invalidEmail", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(invalidEmail)).contentType(jsonContentType))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void register_ValidEmail_200() throws Exception {
		User validEmail = new User(UUID.randomUUID(), "otherUsername", "password", "firstname", "lastname",
				"validEmail@test.me", "address", "phone", null);
		mockMvc.perform(post("/auth/register").content(this.json(validEmail)).contentType(jsonContentType))
				.andExpect(status().isOk());
	}

	@Test
	public void login_validCredentials_succes() throws Exception {
		mockMvc.perform(formLogin("/auth/login").user("username").password("password")).andExpect(status().isOk())
				.andExpect(authenticated()).andExpect(authenticated().withUsername("username"))
				.andExpect(authenticated().withRoles("USER", "ADMIN"));
	}

	@Test
	public void login_invalidPassword_401() throws Exception {
		mockMvc.perform(formLogin("/auth/login").user("invalidusername").password("password"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void login_invalidUsername_401() throws Exception {
		mockMvc.perform(formLogin("/auth/login").user("username").password("invalidpassword"))
				.andExpect(status().isUnauthorized());
	}

	@Override
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}