package ro.ubbcluj.cs.email_client.domain.test;

import junit.framework.TestCase;
import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;
import ro.ubbcluj.cs.email_client.factory.EmailFactory;

public class EmailTest extends TestCase {
	
	public void testCreate() {
		Email email=EmailFactory.createTestEmail();
		assertEquals(EmailFactory.TITLE, email.getTitle());
		assertEquals(EmailFactory.CONTENT, email.getContent());
		assertEquals(EmailFactory.USERNAME, email.getOwner());
		assertEquals(EmailFactory.ID, email.getID());
		assertEquals(Status.UNSENT, email.getStatus());
	}
	public void testCopy(){
		Email email=EmailFactory.createTestEmail();
		Email emailCopy=Email.copy(email);
		assertNotSame(email, emailCopy);
	}

}
