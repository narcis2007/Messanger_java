package ro.ubbcluj.cs.email_client.service.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;
import ro.ubbcluj.cs.email_client.domain.validators.EmailValidator;
import ro.ubbcluj.cs.email_client.domain.validators.UserValidator;
import ro.ubbcluj.cs.email_client.repository.EmailRepository;
import ro.ubbcluj.cs.email_client.repository.UserRepository;
import ro.ubbcluj.cs.email_client.service.MessangerService;

public class EmailServiceTest {

	MessangerService service;
	
	
	/*@Before
	 * public void setUp() {
		service=new EmailClientService(new EmailRepository(new EmailValidator()),new UserRepository(new UserValidator()));
		try {
			service.add_email("title1", "content1");
			service.add_email("testTitle2", "content2");
			service.add_email("testTitle3", "content3");
			
		} catch (ValidationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	*/
	/*
	@Test
	public void testSearchByTitle() {
		Email[] emails=service.searchByTitle("test");
		assertEquals(2, emails.length);
	}
	
	@Test
	public void testEdit() {
		assertTrue(service.edit_email(0, "edited title", "edited content") );
	}
	
	@Test
	public void testDelete() {
		assertTrue(service.delete(2));
	}
	
	@Test
	public void testSend() {
		assertTrue(service.send(0,"admin"));
		assertEquals(Status.SENT, service.get_all()[0].getStatus());
	}
	
	@Test
	public void testFilterByStatus() {
		service.send(0,"admin");
		assertEquals(2, service.filterByStatus(Status.UNSENT).length);
		assertEquals(1, service.filterByStatus(Status.SENT).length);
		service.send(1,"admin");
		assertEquals(1, service.filterByStatus(Status.UNSENT).length);
		assertEquals(2, service.filterByStatus(Status.SENT).length);
	}
	*/
}
