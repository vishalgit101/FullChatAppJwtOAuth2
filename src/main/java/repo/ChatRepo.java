package repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.ChatMessage;

public interface ChatRepo extends JpaRepository<ChatMessage, Long> {
	
	@Query("SELECT cm FROM ChatMessage cm WHERE cm.type= 'PRIVATE_MESSAGE' AND "
			+ "(( cm.sender = :user1 AND cm.recepient = :user2) OR (cm.sender = :user2 AND cm.recepient = :user1)) "
			+ "ORDER BY cm.timeStamp ASC")
	public List<ChatMessage> findPrivateMessageBetweenTwoUsers(@Param("user1") String user1, @Param("user2") String user2);
}
