package messenger.service;

import com.google.common.collect.Lists;
import messenger.models.Email;
import messenger.models.Employee;
import messenger.models.Status;
import messenger.models.User;
import messenger.repository.EmailDao;
import messenger.repository.UserDao;
import messenger.service.utils.Collections;
import messenger.utils.AsyncCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class MessangerService  {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EmailDao emailRepository;
	@Autowired
//	private UserRepository userRepository;
	private UserDao userRepository;
	private User user;
	private static final Logger log = Logger.getLogger( MessangerService.class.getName() );
	protected static ExecutorService executorService =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public List<Employee> get_employees() {
		List<Employee> el=new ArrayList<>();
		el.add(new Employee("1","q","a"));
		el.add(new Employee("2","w","s"));
		return el;
	}

	public MessangerService() {


		this.user=new User("anonymous","");
		log.info("messenger.service created");
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#add_email(java.lang.String, java.lang.String)
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add_email(@RequestBody Map<String,String> data) {
		String title=data.get("title");
		String content=data.get("content");
//		String sender=data.get("sender");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String sender = auth.getName(); //get logged in username
		log.info(sender);
		String receiver=data.get("receiver");
		emailRepository.save(new Email(title,content,sender,receiver));
	}


	@RequestMapping(value = "/findByReceiver", method = RequestMethod.POST)
	public Collection<Email> findByReceiver(@RequestBody Map<String,String> data) {
		String receiver=data.get("receiver");
		return Lists.reverse(Collections.makeList(emailRepository.findByUsername(receiver)));

	}

	@RequestMapping(value = "/getMessageCountByReceiver", method = RequestMethod.POST)
	public Integer getMessageCountByReceiver(@RequestBody Map<String,String> data) {
		String receiver=data.get("receiver");
		return emailRepository.count(receiver);

	}


	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#get_all()
	 */

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(){
		jdbcTemplate.query(
				"SELECT id, username, password FROM utilizatori",
				(rs, rowNum) -> new User(rs.getString("username"), rs.getString("password"))
		).forEach(user -> log.info(user.getUsername()+user.getPassword()));

		return "hello";
	}

	@RequestMapping(value = "/get_all", method = RequestMethod.GET)
	public List<Email> get_all() {
		String username = getUsername();
		log.info(username);
		return Collections.makeList(emailRepository.getAll());
	}

	private String getUsername() {		//trebuie sa fac filtrare la fiecare dupa username
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#searchByTitle(java.lang.String)
	 */
	@RequestMapping(value = "/searchByTitle/{text}", method = RequestMethod.GET)
	public List<Email> searchByTitle(@PathVariable("text") String text) {
		List<Email> emails=Collections.makeList(emailRepository.getAll());
		
		return emails.stream().filter((email)->email.getTitle().contains(text)).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#edit_email(int, java.lang.String, java.lang.String)
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public boolean edit_email(@RequestBody Map<String,String> data) {
		int id=Integer.valueOf(data.get("id"));
		String title=data.get("title");
		String content=data.get("content");
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
	 * @see messanger_server.messenger.service.Service#delete(int)
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public boolean delete(@RequestBody Map<String,Integer> data) {
		int id=data.get("id");
		return emailRepository.delete(id);
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#send(int, java.lang.String)
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)   		//fac o alta metoda, care adauga un mesaj cu toate daele(un fel de add complet)
	public ResponseEntity<Boolean> send(@RequestBody Map<String,String> data) {
		int id=Integer.valueOf(data.get("id"));
		String receiver=data.get("receiver");
		String sender=data.get("sender");
		String title=data.get("title");
		String content=data.get("content");
		Email email=emailRepository.find(id);
		if(email!=null && email.getStatus()==Status.UNSENT){//only unsent messages can be edited
			if(userRepository.getAll().stream().anyMatch((user)->user.getUsername().equals(receiver))==true){//verific daca exista user in messenger.repository
				email.setStatus(Status.SENT);
//				email.setReceiver(1);
			}
			else
				//throw new ServiceException("user does not exist");
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
		else 
			return new ResponseEntity<Boolean>(false,HttpStatus.OK);
		
		return new ResponseEntity<Boolean>(emailRepository.update(email),HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#filterByStatus(messanger_api.messenger.models.Status)
	 */
	@RequestMapping(value = "/filterByStatus/{status}", method = RequestMethod.GET)
	public ResponseEntity<List<Email>> filterByStatus(@PathVariable("status") String stringStatus) {
		Status status;
		try {
			status = Status.valueOf(stringStatus.toUpperCase());
		}
		catch(IllegalArgumentException exception){
			return new ResponseEntity<List<Email>>(HttpStatus.BAD_REQUEST);
		}
		List<Email> emails=Collections.makeList(emailRepository.getAll());

		return new ResponseEntity<List<Email>>(emails.stream().filter((email)->email.getStatus().equals(status)).collect(Collectors.toList()),HttpStatus.OK);
		
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#authenticate(java.lang.String, java.lang.String)
	 */

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public boolean authenticate(@RequestBody Map<String,String> data) {
		String username=data.get("username");
		String password=data.get("password");
		log.info("authenticating");
		User user=userRepository.findByUsername(username);
		if(user!=null&& user.getUsername().equals(username)&&user.getPassword().equals(password))
		{
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see messanger_server.messenger.service.Service#getPage(int)
	 */

	@RequestMapping(value = "/get_page", method = RequestMethod.POST)
	public ResponseEntity<List<Email>> getPage(@RequestBody Map<String,String> data) {
		try {
			synchronized (this) {
				wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int pageNr=Integer.valueOf(data.get("pageNr"));
		int pageSize=Integer.valueOf(data.get("pageSize"));
		String receiver=data.get("receiver");
		List<Email> page= emailRepository.getPage(receiver,pageNr,pageSize);
		if(page==null)
			return new ResponseEntity<List<Email>>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<List<Email>>(page,HttpStatus.OK);
	}
	
	public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback) {
        executorService.submit(() -> {
                	List<Email> res =Collections.makeList(emailRepository.getAll());
                    asyncCallback.onSuccess(res);
        });
		
	}
	@RequestMapping(value = "/get_users", method = RequestMethod.GET)
	public List<User> getUsers() {
		try {
			synchronized (this) {
				wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Collections.makeList(userRepository.getAll());

	}


}
