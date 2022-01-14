package com.example.proiectjavafx;

import domain.FriendRequest;
import domain.Friendship;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import obs.Observer;
import service.MainService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FriendRequestController extends Observer {
    @FXML
    private User currentUser;
    private MainService mainService;
    private final ObservableList<FriendRequest> modelReceived = FXCollections.observableArrayList();
    private final ObservableList<FriendRequest> modelSent = FXCollections.observableArrayList();

    @FXML
    Button acceptBtn;
    @FXML
    Button declineBtn;
    @FXML
    Button deleteBtn;

    @FXML
    private TableColumn<FriendRequest, String> firstNameColumn;
    @FXML
    private TableColumn<FriendRequest, String> lastNameColumn;
    @FXML
    private TableColumn<FriendRequest, String> dateDTOColumn;
    @FXML
    private TableColumn<FriendRequest, String> statutDTOColumn;
    @FXML
    private TableView<FriendRequest> tableView;

    @FXML
    private TableColumn<FriendRequest, String> fName;
    @FXML
    private TableColumn<FriendRequest, String> lName;
    @FXML
    private TableColumn<FriendRequest, String> dateS;
    @FXML
    private TableColumn<FriendRequest, String> statusS;
    @FXML
    private TableView<FriendRequest> sentRequestsView;

    public void setService(MainService service) {
        mainService = service;
        this.mainService.addObserver(this);
        currentUser = mainService.findUser(mainService.getByUsername(mainService.getCurrentUser()).getId());
        initModel();
    }


    @FXML
    public void initialize() {
        setCells(firstNameColumn, lastNameColumn, dateDTOColumn, statutDTOColumn, tableView, modelReceived);

        setCells(fName, lName, dateS, statusS, sentRequestsView, modelSent);
    }

    private void setCells(TableColumn firstNameColumn, TableColumn lastNameColumn, TableColumn dateDTOColumn, TableColumn statutDTOColumn, TableView<FriendRequest> tableView, ObservableList<FriendRequest> model) {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("lastName"));
        dateDTOColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, LocalDateTime>("data"));
        statutDTOColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("statut"));

        tableView.setItems(model);
    }


    private void initModel() {
        List<Friendship> requests = (List<Friendship>) mainService.findFriendRequests(currentUser);
        List<FriendRequest> users = new ArrayList<>();
        List<FriendRequest> usersTo = new ArrayList<>();
        requests.forEach(x -> {
            if (x.getId().getRight().equals(currentUser.getId())) {
                users.add(new FriendRequest(mainService.findUser(x.getId().getLeft()).getFirstName(), mainService.findUser(x.getId().getLeft()).getLastName(), x.getDate(), x.getStatut(), x.getId()));
            }
            if (x.getId().getLeft().equals(currentUser.getId()) && x.getStatut().equals("Pending")) {
                usersTo.add(new FriendRequest(mainService.findUser(x.getId().getRight()).getFirstName(), mainService.findUser(x.getId().getRight()).getLastName(), x.getDate(), x.getStatut(), x.getId()));
            }
        });
        modelReceived.setAll(users);
        modelSent.setAll(usersTo);
    }

    public void handleBtnRequestsDeny() {
        FriendRequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteBtn.setDisable(true);
            mainService.removeFriendship(selected.getId());
            mainService.addFriendship(new Friendship(selected.getId().getLeft(), selected.getId().getRight(), "Declined"));
            modelReceived.remove(selected);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend request deleted successfully!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend request!");
            a.showAndWait();
        }
    }

    public void handleBtnRequestsAccept() {
        FriendRequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteBtn.setDisable(true);
            mainService.removeFriendship(selected.getId());
            mainService.addFriendship(new Friendship(selected.getId().getLeft(), selected.getId().getRight(), "Approved"));
            modelReceived.remove(selected);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend request accepted successfully!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend request!");
            a.showAndWait();
        }


    }

    public void handleBtnRequestsDelete() {
        FriendRequest selected = sentRequestsView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            acceptBtn.setDisable(true);
            declineBtn.setDisable(true);
            mainService.removeFriendship(selected.getId());
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Friend request deleted successfully!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend request!");
            a.showAndWait();
        }
    }

    @Override
    protected void update() {
        initModel();
    }


}
