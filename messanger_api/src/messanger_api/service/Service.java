package messanger_api.service;

import java.util.List;


import messanger_api.models.Email;
import messanger_api.models.Page;
import messanger_api.models.Status;
import utils.AsyncCallback;

public interface Service {

	public static final String SERVICE_HOST = "localhost";
    public static final int SERVICE_PORT = 1234;
    
    public static final String AUTHENTICATE ="authenticate";
	public static final String METHOD = "method";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String GET_ALL = "get_all";
	public static final String GET_PAGE = "get_page";
	public static final String PAGE_NUMBER = "page_nr";
	
	
	void add_email(String title, String content);

	List<Email> get_all();

	List<Email> searchByTitle(String text);

	boolean edit_email(int id, String title, String content);

	boolean delete(int id);

	boolean send(int id, String receiver);

	List<Email> filterByStatus(Status status);

	boolean authenticate(String username, String password);

	Page<Email> getPage(int pageNr);
	
	public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback);

}