package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import filter.JwtFilter;
import service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // can add role based security on method level
public class SecurityConfigs {
	// DI
	private final MyUserDetailsService myUserDetailsService;
	private final JwtFilter jwtFilter;
	private final CorsConfigurationSource corsConfigurationSource;
	
	@Autowired
	public SecurityConfigs(MyUserDetailsService myUserDetailsService, JwtFilter jwtFilter, CorsConfigurationSource corsConfigurationSource) {
		super();
		this.myUserDetailsService = myUserDetailsService;
		this.jwtFilter = jwtFilter;
		this.corsConfigurationSource = corsConfigurationSource;
	}

	// 4 step process 
	
	
	// Security filter chain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Takes HttpSecurity
		
		return http.
			//cors(Customizer.withDefaults())
			csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(this.corsConfigurationSource))
			.authorizeHttpRequests(auth -> 
					auth.requestMatchers("/hello", "/login", "/signup").permitAll()
					.requestMatchers("/api/csrf-token").permitAll()
					.requestMatchers("/api/admin/**").hasRole("ADMIN")
					.requestMatchers("/api/auth/public/**").permitAll()
					.requestMatchers("/auth/**").permitAll()
					.requestMatchers("/oauth2").permitAll()
					.requestMatchers("/ws/**").permitAll()
					.anyRequest().authenticated()
					)
			
			// exception handling
			
			// oauth2 
			
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
			
	}
	
	// UserDetailsService/InMemory
	/*@Bean
	public UserDetailsService userDetails() {
		UserDetails user1 = User.builder()
				.username("Vishal").password("pass123").roles("ADMIN", "MANAGER", "USER").build();
		
		UserDetails user2 = User.builder()
				.username("NoName").password("pass123")
				.roles("USER").build();
		
		return new InMemoryUserDetailsManager(user1, user2);
	}*/
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); 
		
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		
		// Set UserService
		provider.setUserDetailsService(this.myUserDetailsService); // add myUserDetailService, that return userDetails Object or user principal
		
		return provider;
		
	}
	
	// Auth Manager
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
}
