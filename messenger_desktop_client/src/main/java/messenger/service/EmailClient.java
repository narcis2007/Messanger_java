package messenger.service;

import messenger.models.Email;
import messenger.models.Status;
import messenger.models.User;
import messenger.utils.AsyncCallback;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Narcis2007 on 06.12.2015.
 */

@Component
public class EmailClient implements Service {
    private static final Logger log = Logger.getLogger( EmailClient.class.getName() );
    public static final String URL = "http://localhost:8080/";
    private final HttpHost host = new HttpHost("localhost", 8080, "http");
    private User user;

    private RestTemplate restTemplate = new RestTemplate();
    private final AuthHttpComponentsClientHttpRequestFactory requestFactory;

    public static EmailClient emailClient;

    public static EmailClient getEmailClient(){ //singleton
        if(emailClient==null)
            emailClient=new EmailClient();
        return emailClient;
    }




    public EmailClient(){

        log.info("service client created");
        requestFactory=new AuthHttpComponentsClientHttpRequestFactory(host,user);
        restTemplate = new RestTemplate(requestFactory);
    }



    public int getCount(String receiver){

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("receiver",receiver);

        return restTemplate.postForObject(URL + "getMessageCountByReceiver", vars,Integer.class);
    }


    @Override
    public void add_email(String title, String content,String sender, String receiver) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("title",title);
        vars.put("content",content);
        vars.put("sender",sender);
        vars.put("receiver",receiver);
        vars.put("status", String.valueOf(Status.SENT));

        restTemplate.postForObject(URL + "add", vars,Void.class);


    }

    @Override
    public List<Email> get_all() {
        Email[] emails=restTemplate.getForObject(URL+"get_all", Email[].class);
        log.info("fgjghjghjgh");
        return null;

    }

    @Override
    public List<Email> searchByTitle(String text) {
        return null;
    }

    @Override
    public boolean edit_email(int id, String title, String content) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean send(int id, String receiver) {
        return false;
    }

    @Override
    public List<Email> filterByStatus(Status status) {
        return null;
    }

    @Override
    public boolean authenticate(String username, String password) {

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("username",username);
        vars.put("password",password);

        boolean authenticated=restTemplate.postForObject(URL+"authenticate",vars,Boolean.class);
        if(authenticated) {
            user = new User(username, password);
            requestFactory.setUser(user);
        }
        return authenticated;
    }

    @Override
    public Email[] getPage(String receiver, int pageNr, int itemsPerPage) {
        Map<String,String> vars=new HashMap<>();
        vars.put("receiver",receiver);
        vars.put("pageNr",String.valueOf(pageNr));
        vars.put("pageSize",String.valueOf(itemsPerPage));
        return restTemplate.postForObject(URL+"get_page",vars,Email[].class);
    }

    @Override
    public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback) {

    }

    public User[] getUsers() {
        User[] users=restTemplate.getForObject(URL+"get_users", User[].class);
        log.info("getting user list");
        return users;
    }

    public Email[] getEmailsByReceiver(String receiver) {
        Map<String,String> vars=new HashMap<>();
        vars.put("receiver",receiver);
        return restTemplate.postForObject(URL+"findByReceiver",vars,Email[].class);
    }
}
