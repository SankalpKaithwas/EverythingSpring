package com.example.springBoot.jpa.h2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
			RestAuthenticationEntryPoint entryPoint) {

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
						.anyRequest().hasRole("ADMIN"))

						/**
						 * hasRole("ADMIN") is compared against the authority ROLE_ADMIN; Spring adds
						 * the ROLE_ prefix itself.
						 */
				.exceptionHandling(ex -> 
				ex.authenticationEntryPoint(entryPoint))

				// 4. Enable HTTP Basic Authentication
			.httpBasic(basic -> basic.authenticationEntryPoint(entryPoint));

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * On startup - "Using generated security password" line should no longer
	 * appear, because we are using our own UserDetailsService which replaces Boot's
	 * default.
	 */
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		UserDetails userDetails = User.withUsername("user").password(encoder.encode("testuser")).roles("USER").build();

		UserDetails adminDetails = User.withUsername("admin").password(encoder.encode("adminuser")).roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(userDetails, adminDetails);
	}
//	Authorization runs in the security filter chain, before the request reaches your controller. 
//	The user is rejected before your service gets a chance to say "not found".
}

/** A @Bean method creates an object and registers it in Spring's container, and that 
 * object can be handed to any class that asks for its type. What happens, in order:

1. At startup, Spring finds the methods annotated with @Bean in your @Configuration classes.
 Singleton beans are created eagerly, in dependency order.
2. It calls each method once, injecting the method's parameters (for example PasswordEncoder
 into userDetailsService).
3. It stores the returned object. Whoever needs that type gets that same object.*/

/**securityFilterChain(...) runs once at startup and builds the chain. The rules
 *  inside it (like hasRole) are then checked on every request. This is why a mistake
 *  in the config shows up at startup (like the missing-bean errors) but a wrong rule
 *  only shows up when you send a request.*/

/**@Bean vs @Component. Put @Component on a class you write (your entry point). 
 * Use @Bean on a method when you need to construct or configure the object yourself, for 
 * example new BCryptPasswordEncoder(), a class you don't own. */
