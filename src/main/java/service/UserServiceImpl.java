package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import entity.Users;
import model.UserPrincipal;
import repo.UserRepo;

public class UserServiceImpl implements UserService {
	
	// DI: Jwt Service, User Repo, AuthManager Bean
	private final JwtService jwtService;
	private final UserRepo userRepo;
	private final AuthenticationManager authManager;
	
	@Autowired
	public UserServiceImpl(JwtService jwtService, UserRepo userRepo, AuthenticationManager authManager) {
		super();
		this.jwtService = jwtService;
		this.userRepo = userRepo;
		this.authManager = authManager;
	}

	@Override
	public String verify(Users user) { // upon successful verification, token should be generated and given to the frontend
		// get the unauthenticated object
		Authentication authentication = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		
		if(authentication.isAuthenticated()) {
			
			// get the principal
			UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
			
			return this.jwtService.generateToken(principal.getUsername());
		}
		
		return "failed";
		
	}

}
