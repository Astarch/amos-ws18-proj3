package de.pretrendr.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import de.pretrendr.model.QUser;
import de.pretrendr.model.Role;
import de.pretrendr.model.User;
import de.pretrendr.model.pojo.RegUser;

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
		RegUser existingUsername = new RegUser("username", "password", "firstname", "lastname", "other@mail.me",
				"address", "phone");
		mockMvc.perform(post("/auth/register").content(this.json(existingUsername)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_existingUserEmail_409() throws Exception {
		RegUser existingEmail = new RegUser("otherUsername", "password", "firstname", "lastname", "existing@mail.me",
				"address", "phone");
		mockMvc.perform(post("/auth/register").content(this.json(existingEmail)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	public void register_InvalidEmail_406() throws Exception {
		RegUser invalidEmail = new RegUser("otherUsername", "password", "firstname", "lastname", "invalidEmail",
				"address", "phone");
		mockMvc.perform(post("/auth/register").content(this.json(invalidEmail)).contentType(jsonContentType))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void register_ValidEmail_200() throws Exception {
		RegUser validEmail = new RegUser("otherUsername", "password", "firstname", "lastname", "validEmail@test.me",
				"address", "phone");
		mockMvc.perform(post("/auth/register").content(this.json(validEmail)).contentType(jsonContentType))
				.andExpect(status().isOk());

		User newUser = userDAO.findOne(QUser.user.email.eq(validEmail.getEmail()));
		assertNotNull(newUser);
		assertNotNull(newUser.getId());
		assertNotNull(newUser.getRoles());

		assertEquals(newUser.getAddress(), validEmail.getAddress());
		assertEquals(newUser.getEmail(), validEmail.getEmail());
		assertEquals(newUser.getFirstname(), validEmail.getFirstname());
		assertEquals(newUser.getLastname(), validEmail.getLastname());
		assertEquals(newUser.getPhone(), validEmail.getPhone());
		assertEquals(newUser.getUsername(), validEmail.getUsername());
		assertTrue(newUser.isAccountNonExpired());
		assertTrue(newUser.isAccountNonLocked());
		assertTrue(newUser.isCredentialsNonExpired());
		assertTrue(newUser.isEnabled());

		assertTrue(passwordEncoder.matches(validEmail.getPassword(), newUser.getPassword()));
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