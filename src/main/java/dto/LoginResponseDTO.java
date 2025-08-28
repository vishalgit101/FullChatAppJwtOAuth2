package dto;

public class LoginResponseDTO {
	
	private String token;
	
	private UserDTO userDTO;

	public LoginResponseDTO(String token, UserDTO userDTO) {
		super();
		this.token = token; // from response we will grab the jwtToken
		this.userDTO = userDTO;
	}

	public LoginResponseDTO() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [token=" + token + ", userDTO=" + userDTO + "]";
	}
	
}
