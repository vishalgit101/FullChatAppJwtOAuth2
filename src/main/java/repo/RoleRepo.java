package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	
	Role findByRole(String string);
	
	
	
}
