package com.example.proiectjavafx;

import domain.validation.FriendshipValidator;
import domain.validation.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.database.FriendshipDbRepository;
import repository.database.MessageDbRepository;
import repository.database.UserDbRepository;
import service.FriendshipService;
import service.MainService;
import service.MessageService;
import service.UserService;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FriendshipDbRepository r = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "897891iout", new FriendshipValidator());
        UserDbRepository u = new UserDbRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "897891iout", new UserValidator());
        MessageDbRepository m = new MessageDbRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "897891iout");
        MainService s = new MainService(new UserService(u), new FriendshipService(r),new MessageService(m));
        URL url = getClass().getClassLoader().getResource("login-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 720);
        stage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        loginController.setService(s);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}