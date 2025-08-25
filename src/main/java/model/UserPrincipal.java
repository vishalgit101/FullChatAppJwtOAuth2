package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import entity.Role;
import entity.Users;

public class UserPrincipal implements UserDetails {
	
	private Users user;
	
	public UserPrincipal(Users user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// assuming roles in dB are in format of ROLE_roleName
		List<SimpleGrantedAuthority> authoroties = new ArrayList<SimpleGrantedAuthority>();
		Set<Role> userRoles =  this.user.getRoles();
		
		for(Role role : userRoles) {
			authoroties.add(new SimpleGrantedAuthority(role.getRole()));
		}
		
		return authoroties;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() { // username == useremail
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return this.user.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.user.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnabled();
	}
	
	public boolean isOnline() {
		return this.user.isOnline();
	}

}
