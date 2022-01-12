package com.example.proiectjavafx;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.RepoException;
import service.MainService;

import java.io.IOException;

public class SignInController {
    private Stage primaryStage;
    private MainService service;

    @FXML
    private TextField txtUsername,txtFirstname,txtLastname,txtEmail;
    @FXML
    private PasswordField txtPassword;

    public void setService(MainService service){
        this.service = service;
    }
    public void setStage(Stage stage){
        this.primaryStage = stage;
    }

    public void handleBtnBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);
        loginController.setStage(primaryStage);
    }

    public void handleBtnRegister(ActionEvent actionEvent) throws IOException {
        try{
            service.addUser(new User(txtFirstname.getText(),txtLastname.getText(),txtUsername.getText(),txtEmail.getText(),txtPassword.getText()));
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Account created!");
            a.showAndWait();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            LoginController loginController = fxmlLoader.getController();
            loginController.setService(service);
            loginController.setStage(primaryStage);
        }
        catch (RepoException ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }

    }
}
