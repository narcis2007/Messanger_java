package messenger.service;

import messenger.models.Email;
import messenger.models.Status;
import messenger.utils.AsyncCallback;

import java.util.List;

public interface Service {

	void add_email(String title, String content,String sender, String receiver);

	List<Email> get_all();

	List<Email> searchByTitle(String text);

	boolean edit_email(int id, String title, String content);

	boolean delete(int id);

	boolean send(int id, String receiver);

	List<Email> filterByStatus(Status status);

	boolean authenticate(String username, String password);

	Email[] getPage(String receiver, int pageNr, int itemsPerPage);
	
	public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback);

}