package de.pretrendr.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Tristan Schneider
 *
 */
@Configuration
@EnableWebSecurity(debug = false)
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private RESTAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private RESTAuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		auth.userDetailsService(userDetailsService());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable()
		.httpBasic().and()
		.userDetailsService(userDetailsService())
		.authorizeRequests().antMatchers("/api/**").authenticated().and()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
		.formLogin().successHandler(authenticationSuccessHandler).and()
		.formLogin().failureHandler(authenticationFailureHandler);

		// CSRF tokens handling
//		 http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(),
//		 CsrfFilter.class);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return userDetailsService;
	}
}