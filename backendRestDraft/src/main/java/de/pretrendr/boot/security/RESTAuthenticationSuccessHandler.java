package de.pretrendr.boot.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.pretrendr.dataccess.UserDAO;
import de.pretrendr.model.QUser;
import de.pretrendr.model.User;

/**
 * @author Tristan Schneider
 */
@Component
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	UserDAO userDAO;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// clearAuthenticationAttributes(request);
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			response.setContentType("application/json");
			response.getWriter().write(new ObjectMapper()
					.writeValueAsString(userDAO.findOne(QUser.user.username.eq(((User) principal).getUsername()))));
		}
	}
}