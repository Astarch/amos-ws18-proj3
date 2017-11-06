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
import org.springframework.security.web.csrf.CsrfFilter;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;

@Configuration
@EnableWebSecurity(debug = false)
@ComponentScan
public class PretrenderSecurityConfig extends WebSecurityConfigurerAdapter {
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

	// @Override
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.authenticationProvider(authenticationProvider());
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable()
		.httpBasic().and()
		.userDetailsService(userDetailsService())
		.authorizeRequests().antMatchers("/api/**").authenticated().and()
		.authorizeRequests().antMatchers("/login/**").permitAll().and()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
		.formLogin().successHandler(authenticationSuccessHandler).and()
		.formLogin().failureHandler(authenticationFailureHandler);
		// http.logout().logoutSuccessUrl("/");

		// CSRF tokens handling
		 http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(),
		 CsrfFilter.class);
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
//		// @formatter:off
//        http
//            .csrf()
//                .disable()
//            .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/login/**").permitAll()
////                .antMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
////            .formLogin()
////                .loginProcessingUrl("/api/v1/users/login")
////                .usernameParameter("username")
////                .passwordParameter("password")
////                .successHandler(new RESTAuthenticationSuccessHandler())
////                .failureHandler(new RESTAuthenticationFailureHandler())
////                .and()
//            .exceptionHandling()
//                .authenticationEntryPoint(new RESTAuthenticationEntryPoint())
//                .and()
//            .logout()
//                .logoutUrl("/logout")
////                .addLogoutHandler(new RESTLogoutHandler())
//                .invalidateHttpSession(true);
//        // @formatter:on
	// }

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		// authProvider.setPasswordEncoder(encoder());
		return authProvider;
		// return new PretrendrAuthenticationProvider();
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