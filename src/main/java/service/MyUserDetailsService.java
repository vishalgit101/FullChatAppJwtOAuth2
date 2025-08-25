package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import entity.Users;
import model.UserPrincipal;
import repo.UserRepo;

@Component
public class MyUserDetailsService implements UserDetailsService {
	// DI repo
	
	private final UserRepo userRepo;

	@Autowired
	public MyUserDetailsService(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // in jwt filter user email should comes
		
		Users user = this.userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new UserPrincipal(user);
		
	}

}
