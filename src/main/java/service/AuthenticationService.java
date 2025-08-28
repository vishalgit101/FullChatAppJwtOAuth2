package service;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import dto.RegisterRequestDTO;
import dto.UserDTO;
import entity.Role;
import entity.Users;
import exceptions.UserAlreadyExists;
import model.UserPrincipal;
import repo.RoleRepo;
import repo.UserRepo;

@Service
public class AuthenticationService {

	// DI AuthManger Bean and UserRepo
	private AuthenticationManager authManager;
	
	private UserRepo userRepo;
	
	private JwtService jwtService;
	
	private BCryptPasswordEncoder encoder;
	
	private RoleRepo roleRepo;
	
	public AuthenticationService(AuthenticationManager authManager, UserRepo userRepo, JwtService jwtService, RoleRepo roleRepo) {
		super();
		this.authManager = authManager;
		this.userRepo = userRepo;
		this.jwtService = jwtService;
		this.roleRepo = roleRepo;
	}

	public UserDTO signup(RegisterRequestDTO registerRequestDTO) {
		// save the user in the repo
		
		String username = registerRequestDTO.getUsername();
		String email = registerRequestDTO.getEmail();
		String password = registerRequestDTO.getPassword();
		
		// Do Global Exception Handling for Username already existing or email already existing
		if(this.userRepo.existsByUsername(username)) {
			throw new UserAlreadyExists("User with username: " + username +" already exists");
		}
		
		if(this.userRepo.existsByEmail(email)) {
			throw new UserAlreadyExists("User with email: " + email + "already exists");
		}
		
		Users user = new Users();
		
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(this.encoder.encode(password));
		user.setCreatedDate(LocalDateTime.now());
		user.setSignUpMethod("signup");
		
		// Default Role
		Role role = this.roleRepo.findByRole("ROLE_USER");
		user.addRole(role);
		
		this.userRepo.save(user);
		
		return convertToUserDTO(user);
		
	}

	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		
		Users user = this.userRepo.findByUsername(loginRequestDTO.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
		
		Authentication authentication = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
		
		if(authentication.isAuthenticated()) {
			UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
			
			String token = this.jwtService.generateToken(principal.getUsername());
			
			LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
			
			loginResponseDTO.setToken(token); // set Jwt Token
			
			// set UserDto 
			UserDTO userDTO = convertToUserDTO(user);
			loginResponseDTO.setUserDTO(userDTO);
			return loginResponseDTO;
		}
		
		return null;
	}
	
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

	public ResponseEntity<String> logout() { 
		ResponseCookie responseCookie = ResponseCookie.from("JWT", "")
		.httpOnly(true)
		.secure (true)
		.path("/")
		.maxAge (0)
		.sameSite("Strict")
		.build();
		return ResponseEntity.ok().header (HttpHeaders.SET_COOKIE, responseCookie.toString())
		.body("Logged out successfully");
		}
	
	private UserDTO convertToUserDTO(Users user) {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setEmail(user.getEmail());
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		return userDTO;
	}

}
