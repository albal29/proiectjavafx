package com.example.proiectjavafx;

import domain.FriendRequest;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.MainService;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestController {
    @FXML
    private Label friendRequests;
    private User currentUser;
    private MainService mainService;
    private ObservableList<FriendRequest> model= FXCollections.observableArrayList();

    @FXML
    private TableColumn firstNameColumn;
    @FXML
    private TableColumn lastNameColumn;
    @FXML
    private TableColumn dateDTO;
    @FXML
    private TableColumn statutDTO;
    @FXML
    private TableView<FriendRequest> tableView;

    public void setService(MainService service){
        mainService=service;
        currentUser=mainService.findUser(mainService.getByUsername(mainService.getCurrentUser()).getId());
        initModel();
    }


    @FXML
    public void initialize(){

    }

    private void initModel(){
        List<Friendship> requests = (List<Friendship>) mainService.findFriendRequests(currentUser);
        List<FriendRequest> users = new ArrayList<>();
        requests.forEach(x -> {
            if (x.getId().getRight().equals(currentUser.getId())) {
                users.add(new FriendRequest(mainService.findUser(x.getId().getLeft()).getFirstName(), mainService.findUser(x.getId().getLeft()).getLastName(), x.getDate(), x.getStatut(), x.getId()));
            }
        });
        model.setAll(users);

        firstNameColumn.setCellValueFactory(new PropertyValueFactory("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory("lastName"));
        /*dateDTO.setCellValueFactory(new PropertyValueFactory("date"));
        statutDTO.setCellValueFactory(new PropertyValueFactory("statut"));*/

        tableView.setItems(model);
    }

    public void handleBtnRequestsAccept(){
        FriendRequest selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            Friendship friendship = mainService.findFrienship(selected.getId());
            friendship.setStatut("");
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend request deleted successfully!");
            a.showAndWait();
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend request!");
            a.showAndWait();
        }
    }

    public void handleBtnRequestsDeny(){
        FriendRequest selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            Friendship friendship = mainService.findFrienship(selected.getId());
            friendship.setStatut("Approved");
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend request accepted successfully!");
            a.showAndWait();
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend request!");
            a.showAndWait();
        }
    }

}
