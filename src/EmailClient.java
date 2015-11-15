/*
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
*/
import java.util.logging.Logger;


import ro.ubbcluj.cs.data.domain.Validator;
import ro.ubbcluj.cs.email_client.domain.Email;
import ro.ubbcluj.cs.email_client.domain.User;
import ro.ubbcluj.cs.email_client.domain.validators.EmailValidator;
import ro.ubbcluj.cs.email_client.domain.validators.UserValidator;
import ro.ubbcluj.cs.email_client.repository.EmailRepository;
import ro.ubbcluj.cs.email_client.repository.UserRepository;
import ro.ubbcluj.cs.email_client.service.MessangerService;
import ro.ubbcluj.cs.email_client.ui.Console;
import ro.ubbcluj.cs.email_client.ui.ConsoleUI;
import ro.ubbcluj.cs.data.core.AppContext;;

public class EmailClient {
	private static final Logger log = Logger.getLogger( EmailClient.class.getName() );
	
	public static void main(String[] args) { //fac application context
		
		try {
				AppContext context=new AppContext("settings.xml");
				
				//Validator<Email> emailValidator=new EmailValidator();
				
				//String filename=context.getBean(EmailRepository.class);
				//Validator<Email> emailValidator=context.getBean(Validator.class);
				//EmailRepository emailRepository=new EmailRepository(emailValidator,"emails.xml");
				//EmailRepository emailRepository=context.getBean(EmailRepository.class);
				//log.info( "email repository created" );
				
				//Validator<User> userValidator=new UserValidator();
				//UserRepository userRepository=new UserRepository(userValidator,"users.xml");
				//UserRepository userRepository=context.getBean(UserRepository.class);
				//log.info( "user repository created" );
				
				//EmailClientService emailService=new EmailClientService(emailRepository,userRepository);
				//EmailClientService emailService=context.getBean(EmailClientService.class);
				//log.info( "service created" );
				
				//ConsoleUI console=new ConsoleUI(emailService);
				Console console=context.getBean(ConsoleUI.class);
				
				console.run();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("something went wrong");
			
		}
	}
}
