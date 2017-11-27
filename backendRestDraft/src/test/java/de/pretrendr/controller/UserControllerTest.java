package de.pretrendr.controller;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Sets;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.model.Role;
import de.pretrendr.model.User;

public class UserControllerTest extends PretrendrTestBase {

	private User user;
	private List<Role> rolesList;
	private Role userRole;
	private Role adminRole;

	@Before
	public void setup() throws Exception {
		this.userDAO.deleteAll();
		this.roleDAO.deleteAll();
		userRole = roleDAO.save(new Role(UUID.randomUUID(), "USER"));
		adminRole = roleDAO.save(new Role(UUID.randomUUID(), "ADMIN"));
		this.rolesList = Lists.newArrayList(adminRole, userRole);
		this.user = userDAO.save(new User(UUID.randomUUID(), "username", "password", "firstname", "lastname",
				"existing@mail.me", "address", "phone", Sets.newHashSet(rolesList)));
	}

	@Test
	public void getUser_validId_checkJSON() throws Exception {
		String auth1 = Lists.newArrayList(this.user.getAuthorities()).get(0).getAuthority();
		String auth2 = Lists.newArrayList(this.user.getAuthorities()).get(1).getAuthority();

		mockMvc.perform(get("/api/user/get/" + this.user.getId())).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType))
				.andExpect(jsonPath("$.id", is(this.user.getId().toString())))
				.andExpect(jsonPath("$.username", is(this.user.getUsername())))
				.andExpect(jsonPath("$.firstname", is(this.user.getFirstname())))
				.andExpect(jsonPath("$.lastname", is(this.user.getLastname())))
				.andExpect(jsonPath("$.email", is(this.user.getEmail())))
				.andExpect(jsonPath("$.address", is(this.user.getAddress())))
				.andExpect(jsonPath("$.phone", is(this.user.getPhone())))
				.andExpect(jsonPath("$.accountNonExpired", is(this.user.isEnabled())))
				.andExpect(jsonPath("$.accountNonLocked", is(this.user.isEnabled())))
				.andExpect(jsonPath("$.credentialsNonExpired", is(this.user.isEnabled())))
				.andExpect(jsonPath("$.authorities").isArray())
				.andExpect(jsonPath("$.authorities", Matchers.hasSize(2)))
				.andExpect(jsonPath("$.authorities[0].authority", either(is(auth1)).or(is(auth2))))
				.andExpect(jsonPath("$.authorities[1].authority", either(is(auth1)).or(is(auth2))))
				.andExpect(jsonPath("$.password").doesNotExist());
		// @formatter:on
	}

	@Test
	public void getUser_invalidId_404NOTFOUND() throws Exception {
		mockMvc.perform(get("/api/user/get/" + new UUID(0L, 0L))).andExpect(status().isNotFound());
	}

	@Test
	public void getAllUser_valueCheck_success() throws Exception {
		String auth1 = Lists.newArrayList(this.user.getAuthorities()).get(0).getAuthority();
		String auth2 = Lists.newArrayList(this.user.getAuthorities()).get(1).getAuthority();

		mockMvc.perform(get("/api/user/getAll")).andExpect(status().isOk())
		// @formatter:off
				.andExpect(content().contentType(jsonContentType)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(this.user.getId().toString())))
				.andExpect(jsonPath("$[0].username", is(this.user.getUsername())))
				.andExpect(jsonPath("$[0].firstname", is(this.user.getFirstname())))
				.andExpect(jsonPath("$[0].lastname", is(this.user.getLastname())))
				.andExpect(jsonPath("$[0].email", is(this.user.getEmail())))
				.andExpect(jsonPath("$[0].address", is(this.user.getAddress())))
				.andExpect(jsonPath("$[0].phone", is(this.user.getPhone())))
				.andExpect(jsonPath("$[0].enabled", is(this.user.isEnabled())))
				.andExpect(jsonPath("$[0].accountNonExpired", is(this.user.isEnabled())))
				.andExpect(jsonPath("$[0].accountNonLocked", is(this.user.isEnabled())))
				.andExpect(jsonPath("$[0].credentialsNonExpired", is(this.user.isEnabled())))
				.andExpect(jsonPath("$[0].authorities").isArray())
				.andExpect(jsonPath("$[0].authorities", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].authorities[0].authority", either(is(auth1)).or(is(auth2))))
				.andExpect(jsonPath("$[0].authorities[1].authority", either(is(auth1)).or(is(auth2))))
				.andExpect(jsonPath("$[0].password").doesNotExist());
		// @formatter:on
	}
}