package com.example.proiectjavafx;


import com.example.proiectjavafx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.MainService;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private MainService service;
    private Stage primaryStage;

    public void setService(MainService service){
        this.service = service;
    }

    public void setStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    public void handleLoginButton(ActionEvent actionEvent) {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            if(service.logInToAccount(username,password)==true){
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    primaryStage.setTitle("Main");
                    primaryStage.setScene(scene);
                    service.setcurrentUser(username);
                    MainController mainController = fxmlLoader.getController();
                    mainController.setService(service);
                    mainController.setStage(primaryStage);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("No user with the introduced data!");
                a.showAndWait();
            }}
}
