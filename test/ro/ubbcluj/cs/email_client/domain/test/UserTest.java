package ro.ubbcluj.cs.email_client.domain.test;

import junit.framework.TestCase;
import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.User;
import ro.ubbcluj.cs.email_client.factory.EmailFactory;
import ro.ubbcluj.cs.email_client.factory.UserFactory;

public class UserTest extends TestCase {
	
	public void testCreate() {
		User user=UserFactory.createTestUser();
		assertEquals(UserFactory.USERNAME, user.getUsername());
		assertEquals(UserFactory.PASSWORD, user.getPassword());
	}
	public void testCopy(){
		User user=UserFactory.createTestUser();
		User userCopy=User.copy(user);
		assertNotSame(user, userCopy);
	}


}
