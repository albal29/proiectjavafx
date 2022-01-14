package com.example.proiectjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import service.MainService;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane rightSide;

    @FXML
    private Label usernameLbl, firstnameLbl, lastnameLbl;

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

    public void initRight(FXMLLoader loader) throws IOException {
        this.rightSide.setCenter(loader.load());
    }

    @FXML
    public void initModel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view-friends.fxml"));
        rightSide.setCenter(fxmlLoader.load());
        FriendsController friendsController = fxmlLoader.getController();
        friendsController.setService(service);
        friendsController.setRight(rightSide);
    }


    public void handleBtnRequests() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friend-request.fxml"));
        rightSide.setCenter(fxmlLoader.load());
        FriendRequestController friendRequestController = fxmlLoader.getController();
        friendRequestController.setService(service);
    }

    public void handleBtnLogOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Log In");
        primaryStage.setScene(scene);

        LoginController mainController = fxmlLoader.getController();
        mainController.setService(service);
        mainController.setStage(primaryStage);
    }

    public void handleBtnSeeFriends() throws IOException {
        initModel();
    }


}
