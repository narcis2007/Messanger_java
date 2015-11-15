package ro.ubbcluj.cs.email_client.domain.validators.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ro.ubbcluj.cs.email_client.domain.User;
import ro.ubbcluj.cs.email_client.domain.validators.UserValidator;
import ro.ubbcluj.cs.email_client.factory.UserFactory;

public class UserValidatorTest {

	@Test
	public void testCreate() {
		User user=UserFactory.createTestUser();
		//assertFalse(UserValidator.validate(new User(null,null)));
		//assertTrue(UserValidator.validate(user));
	}

}
