package com.example.proiectjavafx;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.RepoException;
import service.MainService;

import java.util.ArrayList;
import java.util.List;

public class FriendsController {

    private MainService service;
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
        initModel();
    }


    private void initModel() {
        List<User> users = service.getUserFriends(service.getByUsername(service.getCurrentUser()).getId());
        model.setAll(users);
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableView.setItems(model);


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
