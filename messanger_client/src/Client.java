import org.omg.CORBA.portable.ApplicationException;

import messanger_client.ui.Console;
import messanger_client.ui.ConsoleUI;
import utils.AppContext;

public class Client {

	public static void main(String[] args) {
		AppContext context=new AppContext("settings.xml"); 
		
		Console console=context.getBean(ConsoleUI.class);
		console.run();

	}

}
