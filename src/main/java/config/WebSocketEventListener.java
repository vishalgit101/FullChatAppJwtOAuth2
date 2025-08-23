package config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import enums.MessageType;
import model.ChatMessage;

@Component
public class WebSocketEventListener {
	// msg broker template DI
	private final SimpMessageSendingOperations messageTemplate;

	@Autowired
	public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
		super();
		this.messageTemplate = messageTemplate;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){ // SessionDisconnectEvent coming from web socket dependency
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage()); // similar to http request header but for websockets
		System.out.println("Header Accessor: " + headerAccessor );
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		if(username != null) {
			logger.info("User Disconnected: {}", username);
			ChatMessage chatMessage = new ChatMessage();
			
			chatMessage.setType(MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messageTemplate.convertAndSend("/topic/public", chatMessage);
			// message will be sent to all the users who are listeing to this channel
		}
		
	}
	
	
}
