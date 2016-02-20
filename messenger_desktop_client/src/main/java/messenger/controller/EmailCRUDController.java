package messenger.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messenger.models.Email;
import messenger.models.User;
import messenger.service.EmailClient;

/**
 * Created by Narcis2007 on 21.12.2015.
 */
public class EmailCRUDController {
    private final EmailClient emailClient;

    public EmailCRUDController(EmailClient emailClient) {
        this.emailClient=emailClient;
    }

    public User[] getUsers() {
        return emailClient.getUsers();
    }

    public Service<User[]> getUsersService()
    {
        return new Service<User[]>() {
            @Override
            protected Task<User[]> createTask() {
                return new Task<User[]>() {
                    @Override
                    protected User[] call() throws Exception {
                        return emailClient.getUsers();
                    }
                };
            }
        };
    }


    public void addMessage(String title, String content, String sender, String receiver) {
        emailClient.add_email(title,content,sender,receiver);
    }

    public Email[] getMessages(String receiver) {
        return emailClient.getEmailsByReceiver(receiver);
    }



    public int getCount(String receiver){
        return emailClient.getCount(receiver);
    }

    public Email[] getPage(String receiver, int pageIndex, int itemsPerPage) {
        return emailClient.getPage(receiver,pageIndex,itemsPerPage);
    }
    public Service<Email[]> getPageService(String receiver, int pageIndex, int itemsPerPage){
        return new Service<Email[]>() {
            @Override
            protected Task<Email[]> createTask() {
                return new Task<Email[]>() {
                    @Override
                    protected Email[] call() throws Exception {
                        return emailClient.getPage(receiver,pageIndex,itemsPerPage);
                    }
                };
            }
        };
    }
}
