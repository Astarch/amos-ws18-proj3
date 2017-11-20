package de.pretrendr.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.pretrendr.dataccess.UserDAO;
import de.pretrendr.model.QUser;
import de.pretrendr.model.User;

@Service("customUserDetailsService")
@Transactional
public class PretrendrUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	public PretrendrUserDetailsService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userDAO.findOne(QUser.user.username.eq(username));
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
}