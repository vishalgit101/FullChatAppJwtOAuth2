package restController;




import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import dto.RegisterRequestDTO;
import dto.UserDTO;
import entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import model.UserPrincipal;
import repo.UserRepo;
import service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	// DI Service layer
	private AuthenticationService authenticationService;
	private UserRepo userRepo;
	
	public AuthController(AuthenticationService authenticationService, UserRepo userRepo) {
		super();
		this.authenticationService = authenticationService;
		this.userRepo = userRepo;
	}

	@GetMapping("/csrf-token")
	public CsrfToken csrfToken(HttpServletRequest request) {
		CsrfToken csrf = (CsrfToken) ( request.getAttribute(CsrfToken.class.getName()));
		return csrf;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserDTO> signup (@RequestBody RegisterRequestDTO registerRequestDTO){
		return ResponseEntity.ok(this.authenticationService.signup(registerRequestDTO));
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
		LoginResponseDTO loginResponseDTO = this.authenticationService.login(loginRequestDTO);
		
		ResponseCookie responseCookie = ResponseCookie.from("JWT", loginResponseDTO.getToken())
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(1*60*60) //1h
				.sameSite("strict")
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(loginResponseDTO.getUserDTO());
		
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(){
		return this.authenticationService.logout();
	}
	
	@GetMapping("/getcurrentuser")
	public ResponseEntity<?> getCurerntUser(@AuthenticationPrincipal UserPrincipal principal){
		
		String username = principal.getUsername();
		
		if(username == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER NOT AUTHORIZED");
		}
		
		Users user = this.userRepo.findByEmail(username).orElseThrow( ()-> new RuntimeException("User not found")); // could do find by username as well
		
		return ResponseEntity.ok(convertToUserDTO(user));
	}

	private UserDTO convertToUserDTO(Users user) {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setEmail(user.getEmail());
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		return userDTO;
	}
	
	@GetMapping("/getonlineusers")
	public ResponseEntity<List<UserDTO>> getOnlineUsers(){
		return ResponseEntity.ok(this.authenticationService.getOnlineUsers());
	}
	
}
