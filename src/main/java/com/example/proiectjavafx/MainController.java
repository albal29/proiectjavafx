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
    private BorderPane rightSide;

    @FXML
    private Label usernameLbl,firstnameLbl,lastnameLbl;

    private MainService service;
    private Stage primaryStage;


    public void setService(MainService service) throws IOException {
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
    public void initModel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view-friends.fxml"));
        rightSide.setCenter(fxmlLoader.load());
        FriendsController friendsController = fxmlLoader.getController();
        friendsController.setService(service);
    }



    public void handleBtnRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friend-request.fxml"));
        rightSide.setCenter(fxmlLoader.load());
        FriendRequestController friendRequestController = fxmlLoader.getController();
        friendRequestController.setService(service);
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

    public void handleBtnSeeFriends(ActionEvent actionEvent) throws IOException {
        initModel();
    }


}
