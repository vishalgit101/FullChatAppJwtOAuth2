package entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import enums.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name =  "chat_messages")
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	private String sender;
	private String recepient;
	private String color;
	
	@Column(nullable = false)
	private LocalDateTime timeStamp;
	
	@Enumerated(EnumType.STRING)
	private MessageType type;
	
	public ChatMessage() {
		super();
	}

	public ChatMessage(Long id, String content, String sender, String recepient, String color, LocalDateTime timeStamp,
			MessageType type) {
		super();
		this.id = id;
		this.content = content;
		this.sender = sender;
		this.recepient = recepient;
		this.color = color;
		this.timeStamp = timeStamp;
		this.type = type;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRecepient() {
		return recepient;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	
	
}
