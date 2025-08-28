package service;

import entity.Users;

public interface UserService {
	String verify(Users user); // this for verifying the users and generating token

	boolean userExitsByUsername(String username);

	void setUserOnlineStatus(String sender, boolean b);
}
