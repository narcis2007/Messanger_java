package messenger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messenger.controller.EmailCRUDController;
import messenger.controller.LoginController;
import messenger.models.User;
import messenger.service.EmailClient;
import messenger.views.AuthenticationView;
import messenger.views.EmailCRUDView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MessengerGuiApplication extends Application{
    private static final Log log = LogFactory.getLog(MessengerGuiApplication.class);
    private Stage stage;
    private Scene scene;
    public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private User user;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("starting stage");
        stage=primaryStage;
        LoginController loginController=new LoginController(EmailClient.getEmailClient());
        AuthenticationView authenticationView=new AuthenticationView(this,loginController);
        scene=new Scene(authenticationView);
        stage.setScene(scene);
        stage.setTitle("Messenger");
        stage.show();

    }

    public void EmailCRUD(){
        log.info("switching to email crud view");
        EmailCRUDController emailCRUDController = new EmailCRUDController(EmailClient.getEmailClient());
        EmailCRUDView emailCRUDView=new EmailCRUDView(this,emailCRUDController);
        scene=new Scene(emailCRUDView);
        stage.setScene(scene);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
