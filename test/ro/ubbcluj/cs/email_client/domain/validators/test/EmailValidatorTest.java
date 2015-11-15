package ro.ubbcluj.cs.email_client.domain.validators.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.validators.EmailValidator;
import ro.ubbcluj.cs.email_client.factory.EmailFactory;

public class EmailValidatorTest {

	@Test
	public void testIsValid() {
		Email email=EmailFactory.createTestEmail();
		//assertTrue(EmailValidator.isValid(email));
		//assertFalse(EmailValidator.isValid(new Email("", null, "")));
	}

}
