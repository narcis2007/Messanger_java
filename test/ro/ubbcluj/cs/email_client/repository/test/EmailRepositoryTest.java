package ro.ubbcluj.cs.email_client.repository.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;
import ro.ubbcluj.cs.email_client.domain.validators.EmailValidator;
import ro.ubbcluj.cs.email_client.factory.EmailFactory;
import ro.ubbcluj.cs.email_client.repository.EmailRepository;

public class EmailRepositoryTest {
	
	/*
	private EmailRepository repository;
	
	@Before
	public void setUp() throws Exception {
		this.repository=new EmailRepository(new EmailValidator());
		try{
			repository.add(EmailFactory.createTestEmail());
		}
		catch(ValidationException exception){
			fail("validation error");
		}
	}
	
	@Test
	public void testAdd() {
		
		assertEquals(1, repository.getCount());
	}
	
	@Test
	public void testUpdate(){
		Email email=EmailFactory.createTestEmail();
		email.setTitle("new title");
		repository.update(email);
		assertEquals(repository.getAll()[0].getTitle(), email.getTitle());
	}
	
	@Test
	public void testGetAll(){
		assertEquals(1, repository.getAll().length);	
	}
	
	@Test
	public void testGetEmailByID(){
		assertEquals(EmailFactory.TITLE, repository.getEmailByID(0).getTitle());
		assertEquals(EmailFactory.TITLE, repository.getEmailByID(0).getTitle());
		assertEquals(EmailFactory.CONTENT, repository.getEmailByID(0).getContent());
		assertEquals(EmailFactory.USERNAME, repository.getEmailByID(0).getOwner());
		assertEquals(EmailFactory.ID, repository.getEmailByID(0).getID());
		assertEquals(Status.UNSENT, repository.getEmailByID(0).getStatus());
	}
	
	@Test
	public void testDelete(){
		assertEquals(1, repository.getAll().length);	
		repository.delete(0);
		assertEquals(0, repository.getAll().length);	
	}
*/
}
