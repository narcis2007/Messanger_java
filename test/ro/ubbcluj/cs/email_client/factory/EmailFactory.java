package ro.ubbcluj.cs.email_client.factory;

/**
 * @author Narcis2007
 *
 */

import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;

public class EmailFactory {
	
	public static final int ID = 0;
	public static final String USERNAME = "testUser";
	public static final String TITLE = "Title";
	public static final String CONTENT = "Content";

	public static Email createTestEmail() {
		Email email= new Email(TITLE,CONTENT,USERNAME);
		email.setID(ID);
		email.setStatus(Status.UNSENT);
		return email;
	}
}
