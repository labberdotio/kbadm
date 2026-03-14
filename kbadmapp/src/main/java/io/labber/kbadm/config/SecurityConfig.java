
//
// Copyright (c) 2019, 2023, 2024, 2025, John Grundback
// All rights reserved.
//

package io.labber.kbadm.config;

import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableWebSecurity
public class SecurityConfig {

	// @Bean
	// // @Order(2)
	// public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	// 	http
	// 		.csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
	// 		.authorizeHttpRequests(
	// 			authorizeRequests -> authorizeRequests
	// 				.anyRequest().permitAll())
	// 				.sessionManagement(sess -> sess
	// 				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
	// 			);
	// 	return http.build();
	// }

}
