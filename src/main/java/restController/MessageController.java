package restController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entity.ChatMessage;
import model.UserPrincipal;
import repo.ChatRepo;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	// ChatRepo
	private ChatRepo chatRepo;

	public MessageController(ChatRepo chatRepo) {
		super();
		this.chatRepo = chatRepo;
	}
	
	@GetMapping			//Earlier: @RequestParam String user1, @RequestParam String user2
	public ResponseEntity<List<ChatMessage>> getPrivateMessages(@AuthenticationPrincipal UserPrincipal principal, @RequestParam String user2) {
		// user1 should be the principal otherwise anyone could be able to see chat between two users
		
		String user1 = principal.getUsername();
		
		List<ChatMessage> messages = this.chatRepo.findPrivateMessageBetweenTwoUsers(user1, user2);
		
		return ResponseEntity.ok(messages);		
	}
	
}
