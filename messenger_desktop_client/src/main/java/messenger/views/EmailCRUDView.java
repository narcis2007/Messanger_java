package messenger.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messenger.MessengerGuiApplication;
import messenger.controller.EmailCRUDController;
import messenger.models.User;

/**
 * Created by Narcis2007 on 21.12.2015.
 */
public class EmailCRUDView extends HBox {
    private static final String ADD = "Add Message";
    private static final String SEE_MESSAGES = "See Messages";
    private final MessengerGuiApplication application;
    private final EmailCRUDController controller;
    private final Button sendMessageButton;
    private final Button viewMessageButton;
    private final ProgressIndicator progress;

    public EmailCRUDView(MessengerGuiApplication messengerGuiApplication, EmailCRUDController emailCRUDController) {
        this.application=messengerGuiApplication;
        this.controller=emailCRUDController;

        ListView<User> list = new ListView<User>();
        ObservableList<Node> children = getChildren();
        VBox vbox=new VBox();
        sendMessageButton = new Button(ADD);
        sendMessageButton.setDisable(true);
        sendMessageButton.setMaxWidth(Double.MAX_VALUE);
        sendMessageButton.setOnAction((event)->{
            Stage stage = new Stage();
            AddMessageView aMessageView=new AddMessageView(application,controller,application.getUser().getUsername(),list.getSelectionModel().getSelectedItem().getUsername());
            Scene scene = new Scene(aMessageView);
            stage.setScene(scene);
            stage.setTitle("Add Message");
            stage.show();

        });
//        children.add(sendMessageButton);
        ObservableList<Node> vchildren = vbox.getChildren();
        vchildren.add(sendMessageButton);
        viewMessageButton = new Button(SEE_MESSAGES);
//        viewMessageButton.setDisable(true);
        viewMessageButton.setMaxWidth(Double.MAX_VALUE);
        viewMessageButton.setOnAction((event)->{
            Stage stage = new Stage();
            MessageView messageView=new MessageView(application,controller,application.getUser().getUsername());
            Scene scene = new Scene(messageView);
            stage.setScene(scene);
            stage.setTitle("View messages");
            stage.show();

        });
//        children.add(viewMessageButton);
        vchildren.add(viewMessageButton);
        children.add(vbox);
        ObservableList<User> items = FXCollections.observableArrayList ();
//        items.addAll(controller.getUsers());

        progress=new ProgressIndicator();
        progress.setVisible(true);
        vchildren.add(progress);

        Service<User[]> service = controller.getUsersService();
        service.setOnSucceeded(e->{
            items.addAll((User[]) e.getSource().getValue());
            progress.setVisible(false);
        });
        service.start();

        list.setItems(items);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
//                System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
                sendMessageButton.setDisable(list.getSelectionModel().getSelectedItem()==null);

            }
        });
        children.add(list);

    }
}
