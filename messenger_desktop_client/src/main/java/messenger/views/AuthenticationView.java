package messenger.views;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import messenger.MessengerGuiApplication;
import messenger.controller.LoginController;
import messenger.models.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Narcis2007 on 21.12.2015.
 */
public class AuthenticationView extends VBox {
    private static final Log log = LogFactory.getLog(AuthenticationView.class);

    private final String LOGIN="Login";
    private static final String CANCEL = "Cancel";

    private Button authButton;
    private PasswordField password;
    private TextField username;
    private MessengerGuiApplication application; //schimb cu graphic controller - INTREB DACA E BUN ASA SAU E ALTA METODA MAI BUNA!
    private LoginController loginController;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;

    public AuthenticationView(MessengerGuiApplication application, LoginController loginController) {
        this.application = application;
        this.loginController = loginController;
        // build views
        ObservableList<Node> children = getChildren();
        // title
        children.add(new Text("Welcome"));
        // username (label + textfield)
        statusLabel=new Label();
        statusLabel.setVisible(false);
        children.add(statusLabel);
        children.add(new Label("Username:"));
        username = new TextField();
        children.add(username);
        // password (label + textfield)
        children.add(new Label("Password:"));
        password = new PasswordField();
        children.add(password);
        // auth button (login or cancel)
        authButton = new Button(LOGIN);
        authButton.setOnAction(authActionHandler);
        children.add(authButton);
        // progress indicator
        progressIndicator = new ProgressIndicator();
        children.add(progressIndicator);
        // set pre-authenticating state

        setState(LOGIN);
        log.info("authentication view prepared");
    }

    private void setState(String authText) {
        authButton.setText(authText);
        boolean authenticating = authText.equals(CANCEL);
        progressIndicator.setVisible(authenticating);
    }


    private Service<Boolean> authService;

    private final EventHandler<ActionEvent> authActionHandler= btnEvent->{
        String authButtonText = authButton.getText();
        log.info(authButtonText + " button triggered");
        if (authButtonText.equals(LOGIN)) {
            setState(CANCEL);
            String usernameText = username.getText();
            String passwordText = password.getText();
            authService = loginController.authService(usernameText, passwordText); // just a reference to an async call/task
            authService.setOnSucceeded(e -> { // prepare what to do when the call succeeds
                Boolean authenticated = (Boolean) e.getSource().getValue();
                log.info("auth service succeeded,result " + authenticated); // executed on app thread
                setState(LOGIN);
                this.application.setUser(new User(usernameText,passwordText));
                if(authenticated==false){
                    statusLabel.setText("wrong username or password!");
                    statusLabel.setVisible(true);
                }
                else
                    this.application.EmailCRUD();//nu cred ca e bun, schimb cu un viewcontroller
            });
            authService.setOnFailed(e -> { // prepare what to do when the call fails
                setState(LOGIN); // executed on app thread
                Throwable exception = e.getSource().getException();
                log.warn("auth service failed", exception);
//                AlertUtils.showError(exception);
            });
            authService.setOnCancelled(e -> { // prepare what to do when the call was cancelled
                setState(CANCEL); // executed on app thread
                log.info("auth service cancelled");
            });
            log.info("starting auth service");
            authService.start(); // start the async call/task (from app thread)
            // the task is executed on background threads
        } else {
            if (authService != null) {
                authService.cancel(); // cancel the call from app thread
                setState(LOGIN);
            }
        }
    };
}
