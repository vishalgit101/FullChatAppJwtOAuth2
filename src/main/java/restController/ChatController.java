package restController;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import entity.ChatMessage;
import enums.MessageType;
import repo.ChatRepo;
import service.UserService;

@Controller // web socket controllers are normal type of controllers and not RestControllers
@RequestMapping
public class ChatController {
	
	// DI Service layer, ChatRepo, messageTemplate
	private UserService userService;
	
	private ChatRepo chatRepo;
	
	private SimpMessageSendingOperations messageTemplate;

	public ChatController(UserService userService, ChatRepo chatRepo) {
		super();
		this.userService = userService;
		this.chatRepo = chatRepo;
	}

	// send normal message
	@MessageMapping("/chat.sendMessage") // this mapping tells what is the url that want to use to invoke this send message method
	@SendTo("/topic/public") 
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		
		if(this.userService.userExitsByUsername(chatMessage.getSender())) {
			
			if(chatMessage.getTimeStamp() == null) {
				chatMessage.setTimeStamp(LocalDateTime.now());
			}
			
			if(chatMessage.getContent() == null) {
				chatMessage.setContent("");
			}
			
			return this.chatRepo.save(chatMessage);
			
		}
		
		return null;
	}
	
	// send added user message
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public") // this is the url to the channel
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		
		if(this.userService.userExitsByUsername(chatMessage.getSender())) {
			// if exits we will store username in the websocket session
			headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
			
			this.userService.setUserOnlineStatus(chatMessage.getSender(), true);
			
			System.out.println("User Added Successfully " + chatMessage.getSender() + " with session Id: " + headerAccessor.getSessionId());
			
			// TimeStamp for when usercame online
			
			chatMessage.setTimeStamp(LocalDateTime.now());
			
			if(chatMessage.getContent() == null) {
				chatMessage.setContent("");
			}
			
			return this.chatRepo.save(chatMessage); // will send to the channel
		}
		
		return null;
	}
	
	@MessageMapping("/chat.sendPrivateMessage") // with this we will set dynamic subscription
	public void sendPrivateMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		
		if(this.userService.userExitsByUsername(chatMessage.getSender()) && this.userService.userExitsByUsername(chatMessage.getRecepient())) {

			if(chatMessage.getTimeStamp() == null) {
				chatMessage.setTimeStamp(LocalDateTime.now());
			}
			
			if(chatMessage.getContent() == null) {
				chatMessage.setContent("");
			}
			
			chatMessage.setType(MessageType.PRIVATE_MESSAGE);
			
			ChatMessage savedMessage = this.chatRepo.save(chatMessage);
			System.out.println("Message saved successfully with Id: " + savedMessage.getId());
			
			// now send the message
			try {
				String recepientDestination = "/user/" + chatMessage.getRecepient() + "/queue/private";
				System.out.println("Sending message to the receoient destination: " + recepientDestination);
				this.messageTemplate.convertAndSend(recepientDestination, savedMessage);
				
				String senderDestination = "/user/" + chatMessage.getSender() + "/queue/private";
				System.out.println("Sending message to the receoient destination: " + senderDestination);
				this.messageTemplate.convertAndSend(senderDestination, savedMessage);
			} catch (Exception e) {
				System.out.println("ERROR occured while sending the message: " + e.getMessage());
				e.printStackTrace();
			}	
		}else {
			System.out.println("ERROR: Sender " + chatMessage.getSender() + " or recepient " + chatMessage.getRecepient() + " does not exists");
		}
	}
}
