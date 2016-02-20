package messenger.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messenger.service.EmailClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Narcis2007 on 21.12.2015.
 */
public class LoginController {

    private final EmailClient emailClient;
    private static final Log log = LogFactory.getLog(LoginController.class);

    public LoginController(EmailClient emailClient) {
        this.emailClient=emailClient;
    }

    public Service<Boolean> authService(String username, String password) {
        return new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                log.info("creating auth task");
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        log.info("executing auth task");
                        return emailClient.authenticate(username,password);
                    }
                };
            }
        };
    }
}
