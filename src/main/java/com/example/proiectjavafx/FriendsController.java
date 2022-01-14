package com.example.proiectjavafx;

import domain.Friendship;
import domain.Message;
import domain.Tuple;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import obs.Observer;
import repository.RepoException;
import service.MainService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsController extends Observer {

    private MainService service;
    ObservableList<User> model = FXCollections.observableArrayList();
    private BorderPane right;

    public void setRight(BorderPane right) {
        this.right = right;
    }

    @FXML
    private TextField txtSearchUser;
    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TextArea txtMessage;

    public void setService(MainService service) {

        this.service = service;
        this.service.addObserver(this);
        initModel();
    }


    private void initModel() {
        List<User> users = service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId());
        model.setAll(users);
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableView.setItems(model);


    }

    public void handleRemoveFriendButton() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.removeFriendship(new Tuple(selected.getId(), service.getByUsername(service.getCurrentUser()).getId()));
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend deleted successfully!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select an user!");
            a.showAndWait();
        }
    }


    public void handlebtnAddFriend() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId()).contains(selected)) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("User already in friends list!");
                a.showAndWait();
            } else {
                service.addFriendship(new Friendship(service.getByUsername(service.getCurrentUser()).getId(), selected.getId()));
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Friend request sent!");
                a.showAndWait();
            }

        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select an user!");
            a.showAndWait();
        }


    }


    public void btnHandleSearch() {
        try {
            List<User> users = new ArrayList<>();
            users.add(service.getByUsername(txtSearchUser.getText()));
            model.setAll(users);

        } catch (RepoException repoException) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("User with given username does not exist!");
            a.showAndWait();
        }
    }

    public void handleBtnChat() throws IOException {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.remObserver(this);
            FXMLLoader fxmlLoader2 = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
            right.setCenter(fxmlLoader2.load());
            ChatController chatController = fxmlLoader2.getController();
            chatController.setService(service, selected.getUserName());

        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select an user!");
            a.showAndWait();
        }

    }

    public void handleBtnSend() {
        ArrayList<User> list = new ArrayList<>(tableView.getSelectionModel().getSelectedItems());
        if (list.size() > 0) {
            service.saveMsg(new Message(service.getByUsername(service.getCurrentUser()), list, txtMessage.getText(), null));
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Message sent!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select minimum one user!");
            a.showAndWait();
        }
    }

    @Override
    protected void update() {
        initModel();
    }
}
