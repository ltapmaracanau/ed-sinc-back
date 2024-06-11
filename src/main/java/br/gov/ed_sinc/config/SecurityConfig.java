package br.gov.ed_sinc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(authorizeRequests -> 
			authorizeRequests
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/usuarios/equipeCoordenadores").permitAll()
				.requestMatchers(HttpMethod.GET, "/images/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
				.anyRequest().authenticated()
		)
		.cors(cors -> cors.configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues()))
		.sessionManagement(sessionManagement -> 
			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		)
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
