package exceptions;

public class ErrorDetails {
	private String message;

	public ErrorDetails(String message) {
		super();
		this.message = message;
	}
	
	public ErrorDetails() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}