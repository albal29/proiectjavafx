package com.example.proiectjavafx;

import domain.DTO;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.MainService;

import java.util.List;

public class MainController {
    @FXML
    private Label usernameLbl,firstnameLbl,lastnameLbl;

    private MainService service;
    private Stage primaryStage;
    ObservableList<DTO> model = FXCollections.observableArrayList();


    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableView<DTO> tableView;

    public void setService(MainService service){
        this.service = service;
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
        List<DTO> users = service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId());
        model.setAll(users);
    }



    public void handleBtnRequests(ActionEvent actionEvent) {
    }

    public void handleBtnLogOut(ActionEvent actionEvent) {
    }

    public void handleBtnSeeFriends(ActionEvent actionEvent) {
    }

    public void handleRemoveFriendButton(ActionEvent actionEvent) {
    }

    public void handlebtnAddFriend(ActionEvent actionEvent) {
    }

    public void btnHandleSearch(ActionEvent actionEvent) {
    }

    public void handleBtnChat(ActionEvent actionEvent) {
    }
}
