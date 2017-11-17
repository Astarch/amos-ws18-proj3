package de.pretrendr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.pretrendr.boot.Application;
import de.pretrendr.usermanagement.dataccess.RoleDAO;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AuthenticationControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private User user;

	private List<Role> RolesList = new ArrayList<>();

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

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
	//
	// @Test
	// public void readBookmarks() throws Exception {
	// mockMvc.perform(get("/" + userName +
	// "/bookmarks")).andExpect(status().isOk())
	// .andExpect(content().contentType(contentType)).andExpect(jsonPath("$",
	// hasSize(2)))
	// .andExpect(jsonPath("$[0].id", is(this.RolesList.get(0).getId().intValue())))
	// .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
	// .andExpect(jsonPath("$[0].description", is("A description")))
	// .andExpect(jsonPath("$[1].id", is(this.RolesList.get(1).getId().intValue())))
	// .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
	// .andExpect(jsonPath("$[1].description", is("A description")));
	// }
	//
	// @Test
	// public void createBookmark() throws Exception {
	// String bookmarkJson = json(new Bookmark(this.account, "http://spring.io",
	// "a bookmark to the best resource for Spring news and information"));
	//
	// this.mockMvc.perform(post("/" + userName +
	// "/bookmarks").contentType(contentType).content(bookmarkJson))
	// .andExpect(status().isCreated());
	// }

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}