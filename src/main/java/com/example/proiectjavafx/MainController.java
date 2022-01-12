package com.example.proiectjavafx;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import repository.RepoException;
import service.MainService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainController {
    @FXML
    private AnchorPane view_friends;

    @FXML
    private Label usernameLbl,firstnameLbl,lastnameLbl;

    private MainService service;
    private Stage primaryStage;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private TextField txtSearchUser;
    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableView<User> tableView;

    public void setService(MainService service){
        this.service = service;
        this.usernameLbl.setText(service.getCurrentUser());
        this.firstnameLbl.setText(service.getByUsername(service.getCurrentUser()).getFirstName());
        this.lastnameLbl.setText(service.getByUsername(service.getCurrentUser()).getLastName());
        initModel();
    }


    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableView.setItems(model);


    }

    private void initModel() {
        List<User> users = service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId());
        model.setAll(users);
    }



    public void handleBtnRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friend-request.fxml"));

        fxmlLoader.setRoot(view_friends);

        fxmlLoader.load();

        FriendRequestController mainController = fxmlLoader.getController();
        mainController.setService(service);
    }

    public void handleBtnLogOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Log In");
        primaryStage.setScene(scene);

        LoginController mainController = fxmlLoader.getController();
        mainController.setService(service);
        mainController.setStage(primaryStage);
    }

    public void handleBtnSeeFriends(ActionEvent actionEvent) {
        initModel();
    }

    public void handleRemoveFriendButton(ActionEvent actionEvent) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            service.removeFriendship(new Tuple(selected.getId(),service.getByUsername(service.getCurrentUser()).getId()));
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend deleted successfully!");
            a.showAndWait();
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select an user!");
            a.showAndWait();
        }
        }



    public void handlebtnAddFriend(ActionEvent actionEvent) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            if(service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId()).contains(selected))
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("User already in friends list!");
                a.showAndWait();
            }

            else{
                service.addFriendship(new Friendship(selected.getId(),service.getByUsername(service.getCurrentUser()).getId()));
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Friend request sent!");
                a.showAndWait();
            }

            }

        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select an user!");
            a.showAndWait();
        }


        }


    public void btnHandleSearch(ActionEvent actionEvent) {
            try{
                List<User> users = new ArrayList<>();
                users.add(service.getByUsername(txtSearchUser.getText().toString()));
                model.setAll(users);

            }
            catch(RepoException repoException){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("User with given username does not exist!");
                a.showAndWait();
            }
    }

    public void handleBtnChat(ActionEvent actionEvent) {
    }
}
