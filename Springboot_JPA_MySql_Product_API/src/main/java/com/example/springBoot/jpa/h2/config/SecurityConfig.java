package com.example.springBoot.jpa.h2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

		// 1. Disable CSRF because this is a stateless REST API (not browser form
		// sessions)
		httpSecurity.csrf(AbstractHttpConfigurer::disable)

				// 2. Enforce stateless session management (no JSESSIONID cookies)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				/**
				 * Prevents Spring Security from creating HTTP sessions in memory, ensuring
				 * every request is evaluated independently via its Authorization header.
				 */

				// 3. Define endpoint authorization rules
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						
						// Allow anyone (even unauthenticated users) to read/search products
		                //.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

						// Allow ADMIN and USER to read/search products
						.requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "USER")

						// Restrict creating, updating, and deleting products to ADMIN role only
						.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

						/**
						 * hasRole("ADMIN") is compared against the authority ROLE_ADMIN; Spring adds
						 * the ROLE_ prefix itself.
						 */

						// Any other request must be authenticated
						.anyRequest().authenticated())

				// 4. Enable HTTP Basic Authentication
				.httpBasic(Customizer.withDefaults());

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * On startup - "Using generated security password" line should no longer appear, because we
	 * are using our own UserDetailsService which replaces Boot's default.
	 */
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		UserDetails userDetails = User.withUsername("user")
				.password(encoder.encode("testuser"))
				.roles("USER")
				.build();

		UserDetails adminDetails = User.withUsername("admin")
				.password(encoder.encode("adminuser"))
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(userDetails, adminDetails);
	}

}
