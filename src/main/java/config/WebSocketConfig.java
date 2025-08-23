package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// requests will go through this first before going to the controller
@Configuration
@EnableWebSocketMessageBroker // enables websocket over stomp
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override // add the stomp registry end point and for SOCKJS fallback
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/ws");
		
	}

	@Override // 
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		registry.setApplicationDestinationPrefixes("/app"); //  any request coming with /app will be routed to the controller with same mapping
		registry.enableSimpleBroker("/topic"); // used as a way to subscribe
		
	}
	
	
	
}
