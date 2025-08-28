package dto;

public class UserDTO {
	private Long id;
	private String username;
	private String email;
	private boolean isOnline;
	public UserDTO(Long id, String username, String email, boolean isOnline) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.isOnline = isOnline;
	}
	public UserDTO() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", email=" + email + ", isOnline=" + isOnline + "]";
	}
	
	
}
