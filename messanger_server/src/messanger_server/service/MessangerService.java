package messanger_server.service;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import messanger_api.models.Email;
import messanger_api.models.Page;
import messanger_api.models.Status;
import messanger_api.models.User;
import messanger_api.service.Service;
import messanger_server.repository.EmailRepository;
import messanger_server.repository.UserRepository;
import messanger_server.service.utils.Collections;
import utils.AsyncCallback;
import utils.ServiceException;


public class MessangerService implements Service {

	private EmailRepository emailRepository;
	private UserRepository userRepository;
	private User user;
	private static final Logger log = Logger.getLogger( MessangerService.class.getName() );
	protected static ExecutorService executorService =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	public MessangerService(EmailRepository emailRepository, UserRepository userRepository) {
		this();
		this.emailRepository=emailRepository;
		this.userRepository=userRepository;
	}
	public MessangerService() {
		this.user=new User("anonymous","");
		log.info("service created");
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#add_email(java.lang.String, java.lang.String)
	 */
	@Override
	public void add_email(String title, String content) {
		emailRepository.save(new Email(title,content,user.getUsername()));
		
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#get_all()
	 */
	@Override
	public List<Email> get_all() {
		return Collections.makeList(emailRepository.getAll());
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#searchByTitle(java.lang.String)
	 */
	@Override
	public List<Email> searchByTitle(String text) {
		List<Email> emails=Collections.makeList(emailRepository.getAll());
		
		return emails.stream().filter((email)->email.getTitle().contains(text)).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see messanger_server.service.Service#edit_email(int, java.lang.String, java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#delete(int)
	 */
	@Override
	public boolean delete(int id) {
		
		return emailRepository.delete(id);
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#send(int, java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#filterByStatus(messanger_api.models.Status)
	 */
	@Override
	public List<Email> filterByStatus(Status status) {
		List<Email> emails=Collections.makeList(emailRepository.getAll());

		return emails.stream().filter((email)->email.getStatus().equals(status)).collect(Collectors.toList());
		
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String username, String password) {
		log.info("authenticating");
		User user=userRepository.findByUsername(username);
		if(user!=null&& user.getUsername().equals(username)&&user.getPassword().equals(password))
		{
			this.user=user;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see messanger_server.service.Service#getPage(int)
	 */
	@Override
	public Page<Email> getPage(int pageNr) {
		return emailRepository.getPage(pageNr);
	}
	
	public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback) {
        executorService.submit(() -> {
                	List<Email> res =Collections.makeList(emailRepository.getAll());
                    asyncCallback.onSuccess(res);
        });
		
	}

}
