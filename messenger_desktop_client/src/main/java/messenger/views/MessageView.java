package messenger.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import messenger.MessengerGuiApplication;
import messenger.controller.EmailCRUDController;
import messenger.models.Email;

/**
 * Created by Narcis2007 on 29.12.2015.
 */
public class MessageView extends VBox{
    private final MessengerGuiApplication application;
    private final EmailCRUDController controller;
    private final String receiver;
    private final ProgressIndicator progress;

    public MessageView(MessengerGuiApplication application, EmailCRUDController controller, String receiver) {
        this.application = application;
        this.controller = controller;
        this.receiver = receiver;

        Label label = new Label("Messages Received");
        ObservableList<Node> children = getChildren();
        children.add(label);
        progress = new ProgressIndicator();

        children.add(progress);
        TableView table = new TableView();
        TableColumn senderCol = new TableColumn("Sender");
        TableColumn titleCol = new TableColumn("Title");
        TableColumn contentCol = new TableColumn("Content");


        ObservableList<Email> items = FXCollections.observableArrayList ();//ce parametri dau aici?
        items.addAll(controller.getMessages(receiver));

        table.setItems(items);

        table.getColumns().addAll(senderCol, titleCol, contentCol);

        senderCol.setCellValueFactory(
                new PropertyValueFactory<Email,String>("sender")
        );
        titleCol.setCellValueFactory(
                new PropertyValueFactory<Email,String>("title")
        );
        contentCol.setCellValueFactory(
                new PropertyValueFactory<Email,String>("content")
        );

        Pagination pagination = new Pagination((int) Math.ceil(((double)controller.getCount(receiver))/ getItemsPerPage()));
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {

                return createPage(pageIndex);
            }
        });
        children.add(pagination);
    }

    public int getItemsPerPage() {
        return 2;
    }

    public Node createPage(int pageIndex) {
        TableView table = new TableView();
//        for (int i = page; i < page + itemsPerPage(); i++) {
            TableColumn senderCol = new TableColumn("Sender");
            TableColumn titleCol = new TableColumn("Title");
            TableColumn contentCol = new TableColumn("Content");


            ObservableList<Email> items = FXCollections.observableArrayList ();//ce parametri dau aici?
//            items.addAll(controller.getPage(receiver,pageIndex,itemsPerPage()));



        progress.setVisible(true);
        Service<Email[]> service = controller.getPageService(receiver, pageIndex, getItemsPerPage());
        service.setOnSucceeded(e->{
            items.addAll((Email[]) e.getSource().getValue());
            progress.setVisible(false);
        });
        service.start();

            table.setItems(items);

            table.getColumns().addAll(senderCol, titleCol, contentCol);

            senderCol.setCellValueFactory(
                    new PropertyValueFactory<Email,String>("sender")
            );
            titleCol.setCellValueFactory(
                    new PropertyValueFactory<Email,String>("title")
            );
            contentCol.setCellValueFactory(
                    new PropertyValueFactory<Email,String>("content")
            );
            getChildren().add(table);

//        }
        return table;
    }


}
