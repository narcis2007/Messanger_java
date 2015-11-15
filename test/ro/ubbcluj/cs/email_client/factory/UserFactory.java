package ro.ubbcluj.cs.email_client.factory;

import ro.ubbcluj.cs.email_client.domain.User;

public class UserFactory {
	public static final String USERNAME = "Narcis";
	public static final String PASSWORD = "password";

	public static User createTestUser() {
		User user= new User(USERNAME,PASSWORD);
		return user;
	}
}
