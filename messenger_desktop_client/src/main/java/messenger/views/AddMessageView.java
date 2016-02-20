package messenger.views;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import messenger.MessengerGuiApplication;
import messenger.controller.EmailCRUDController;

/**
 * Created by Narcis2007 on 25.12.2015.
 */
public class AddMessageView extends VBox {

    private static final String SUBMIT = "Submit";
    private final MessengerGuiApplication application;
    private final EmailCRUDController controller;
    private String sender;
    private String receiver;

    public AddMessageView(MessengerGuiApplication application, EmailCRUDController controller, String sender, String receiver) {
        this.application = application;
        this.controller = controller;
        this.sender = sender;
        this.receiver = receiver;

        ObservableList<Node> children = getChildren();
        // title
        children.add(new Text("Send a message to "+ receiver +" from "+sender));
        Label statusLabel = new Label();
        statusLabel.setVisible(false);
        children.add(statusLabel);
        children.add(new Label("Title:"));
        TextField title = new TextField();
        children.add(title);
        children.add(new Label("Content:"));
        TextField content = new TextField();
        children.add(content);
        // auth button (login or cancel)
        Button submitButton = new Button(SUBMIT);
        submitButton.setOnAction((event)->{
            controller.addMessage(title.getText(),content.getText(),sender,receiver);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();

        });  //verific sa fac si cu cancelable action ca la authentication
        children.add(submitButton);


//        log.info(view chan);
    }
}
