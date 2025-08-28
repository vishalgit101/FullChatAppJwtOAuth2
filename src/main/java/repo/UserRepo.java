package repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Users;
import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<Users, Long>  {
	
	Optional<Users> findByUsername(String username);
	Optional<Users> findByEmail(String email);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("UPDATE Users u SET u.isOnline = :isOnline WHERE u.username = :username")
	public void updateUserOnlineStatus(@Param("username") String username, @Param("isOnline") boolean isOnline);
	
}
