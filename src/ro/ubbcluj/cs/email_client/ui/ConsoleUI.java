package ro.ubbcluj.cs.email_client.ui;
import ro.ubbcluj.cs.data.core.ServiceException;
import ro.ubbcluj.cs.data.domain.ValidationException;
import ro.ubbcluj.cs.data.repository.Page;
import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.Status;
import ro.ubbcluj.cs.email_client.service.MessangerService;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConsoleUI implements Console {

	private MessangerService service;
	private Scanner scanIn;
	
	private static final Logger log = Logger.getLogger( ConsoleUI.class.getName() );
	
	
	public ConsoleUI(MessangerService service) {
		this();
		this.service=service;
		
	}
	public ConsoleUI() {
		scanIn = new Scanner(System.in);
		log.info("console created");
	}
	public boolean login(){
		String username,password;
		System.out.println("username:");
		username=scanIn.nextLine();
		System.out.println("password:");
		password=scanIn.nextLine();
		return service.authenticate(username,password);
	}
	
	public void showOptions(){
		System.out.println("1. add email");
		System.out.println("2. show emails");
		System.out.println("3. search by title");
		System.out.println("4. edit unsent email");
		System.out.println("5. delete email");
		System.out.println("6. send email");
		System.out.println("7. filter emails by status");
		System.out.println("8. show by page");
		System.out.println("0. exit");
		System.out.println("> ");
		
	}
	
	/* (non-Javadoc)
	 * @see ro.ubbcluj.cs.email_client.ui.Console#run()
	 */
	@Override
	public void run()
	{
		if (login()) {
			log.info("succesful authentication");
			
			String option = new String("-1");
			while (option.equals("0") == false) {
				showOptions();

				option = scanIn.nextLine();
				switch (option) {
				case "1":
					add();
					break;

				case "2":
					showAll();
					break;
				case "3":
					searchByTitle();
					break;
				case "4":
					edit();
					break;
				case "5":
					delete();
					break;
				case "6":
					send();
					break;
				case "7":
					filterByStatus();
					break;
				case "8":
					showByPage();
					break;

				}
			}
		}
		else{
			log.info("authentication failed");
			System.out.println("invalid user ");
			}
	}

	private void showByPage() {
		System.out.println("page:");
		int pageNr=scanIn.nextInt();
		scanIn.nextLine();
		Page<Email> page=service.getPage(pageNr);
		if(page==null)
			System.out.println("sorry, page does not exist");
		else
			page.getElements().parallelStream().forEachOrdered(((email)->printEmail(email)));
		
	}
	private void filterByStatus() {
		System.out.println("1 sent \n");
		System.out.println("2 unsent \n");
		String option;
		System.out.println("> ");
		option=scanIn.nextLine();
		List<Email> emails;
		if(option.equals("1")){
			emails=service.filterByStatus(Status.SENT);
		}
		else
			emails=service.filterByStatus(Status.UNSENT);
		emails.stream().forEachOrdered((email)->printEmail(email));
			
	}

	private void send() {
		int id;
		System.out.println("email id: ");
		id=scanIn.nextInt();
		scanIn.nextLine();
		System.out.println("send to: ");
		String receiver=scanIn.nextLine();
		
		try {
			if(service.send(id,receiver)==false)
				System.out.println("the email has already been sent");
			else
				System.out.println("sent \n");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
		
	}

	private void delete() {
		int id;
		System.out.println("email id: ");
		id=scanIn.nextInt();
		scanIn.nextLine();
		
		if(service.delete(id)==false)
			System.out.println("the email with the given id does not exist");
		else
			System.out.println("deleted \n");
		
	}

	private void edit() {
		int id;
		System.out.println("email id: ");
		id=scanIn.nextInt();
		scanIn.nextLine();
		String title,content;
		System.out.println("title: ");
		title=scanIn.nextLine();
		System.out.println("content: ");
		content=scanIn.nextLine();
		
		try {
			if(service.edit_email(id,title,content)==false)
				System.out.println("the email with the given id was not found or can't be edited");
		} catch (ValidationException e) {
			printErrors(e);
		}
	}
	private void printErrors(ValidationException e) {
		e.getErrors().getAll().stream().forEach((error)->System.out.println(error));
	}

	private void searchByTitle() {
		String title,content;
		int ok=0;
		System.out.println("title: ");
		title=scanIn.nextLine();
		List<Email> emails=service.searchByTitle(title);

		emails.stream().forEach((email)->printEmail(email));
		if(emails.size()==0)
			System.out.println("none matched that text");
	}

	private void showAll() {
		List<Email> emails=service.get_all();
		emails.stream().forEachOrdered((email)->printEmail(email));
		
	}

	private synchronized void printEmail(Email email) {
		System.out.println("email " + email.getID());
		System.out.println("title " + email.getTitle());
		System.out.println("content " + email.getContent());
		System.out.println("added by " + email.getOwner());
		System.out.println("sent to " + email.getReceiver());
		System.out.println("status " + email.getStatus());
		System.out.println("----------------------------");
	}

	private void add() {
		String title,content;
		System.out.println("title: ");
		title=scanIn.nextLine();
		System.out.println("content: ");
		content=scanIn.nextLine();
		try {
			service.add_email(title,content);
		} catch (ValidationException e) {
			printErrors(e);
		}
	}

}
