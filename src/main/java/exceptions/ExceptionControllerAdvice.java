package exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // for global exception handling
public class ExceptionControllerAdvice {
	
	@ExceptionHandler(UserAlreadyExists.class)
	public ResponseEntity<ErrorDetails> userAlreadyExists(UserAlreadyExists ex){
		System.out.println("Exception Controller Advice");
		return ResponseEntity.badRequest().body(new ErrorDetails(ex.getMessage()));
	}

}
