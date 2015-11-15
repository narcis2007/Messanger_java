package ro.ubbcluj.cs.email_client.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ro.ubbcluj.cs.data.utils.Collections;
import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;
import ro.ubbcluj.cs.email_client.domain.User;
import ro.ubbcluj.cs.email_client.repository.EmailRepository;
import ro.ubbcluj.cs.email_client.repository.UserRepository;
import ro.ubbcluj.cs.email_client.ui.ConsoleUI;
import ro.ubbcluj.cs.data.core.ServiceException;
import ro.ubbcluj.cs.data.domain.ValidationException;
import ro.ubbcluj.cs.data.repository.Page;

public class MessangerService {

	private EmailRepository emailRepository;
	private UserRepository userRepository;
	private User user;
	private static final Logger log = Logger.getLogger( MessangerService.class.getName() );
	
	public MessangerService(EmailRepository emailRepository, UserRepository userRepository) {
		this();
		this.emailRepository=emailRepository;
		this.userRepository=userRepository;
	}
	public MessangerService() {
		this.user=new User("anonymous","");
		log.info("service created");
	}

	public void add_email(String title, String content) {
		emailRepository.save(new Email(title,content,user.getUsername()));
		
	}

	public List<Email> get_all() {
		return Collections.makeList(emailRepository.getAll());
	}

	public List<Email> searchByTitle(String text) {
		List<Email> emails=Collections.makeList(emailRepository.getAll());
		
		return emails.stream().filter((email)->email.getTitle().contains(text)).collect(Collectors.toList());
	}
	
	public boolean edit_email(int id, String title, String content) {
		Email email=Email.copy(emailRepository.find(id));
			if(email!=null && email.getStatus()==Status.UNSENT){//only unsent messages can be edited
				email.setTitle(title);
				email.setContent(content);	
			}
			else 
				return false;
		return emailRepository.update(email);
	}

	public boolean delete(int id) {
		
		return emailRepository.delete(id);
	}

	public boolean send(int id, String receiver) {
		Email email=emailRepository.find(id);
		if(email!=null && email.getStatus()==Status.UNSENT){//only unsent messages can be edited
			if(userRepository.getAll().stream().anyMatch((user)->user.getUsername().equals(receiver))){//verific daca exista user in repository
				email.setStatus(Status.SENT);
				email.setReceiver(receiver);
			}
			else
				throw new ServiceException("user does not exist");
		}
		else 
			return false;
		
		return emailRepository.update(email);
	}

	public List<Email> filterByStatus(Status status) {
		List<Email> emails=Collections.makeList(emailRepository.getAll());

		return emails.stream().filter((email)->email.getStatus().equals(status)).collect(Collectors.toList());
		
	}

	public boolean authenticate(String username, String password) {
		User user=userRepository.findByUsername(username);
		if(user!=null&& user.getUsername().equals(username)&&user.getPassword().equals(password))
		{
			this.user=user;
			return true;
		}
		return false;
	}

	public Page<Email> getPage(int pageNr) {
		return emailRepository.getPage(pageNr);
	}

}
