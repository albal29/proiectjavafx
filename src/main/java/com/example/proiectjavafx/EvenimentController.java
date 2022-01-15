package com.example.proiectjavafx;

import domain.Eveniment;
import domain.InvitationEveniment;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.MainService;

import java.time.LocalDate;
import java.util.Collection;

public class EvenimentController {
    private User currentUser;
    private MainService mainService;
    private final ObservableList<Eveniment> model = FXCollections.observableArrayList();

    @FXML
    Button SaveBtn;
    @FXML
    TextField txtTitle;
    @FXML
    TextField txtLocation;
    @FXML
    TextArea txtDescription;
    @FXML
    DatePicker eventDate;

    @FXML
    private TableColumn<Eveniment, String> titleColumn;
    @FXML
    private TableColumn<Eveniment, String> descriptionColumn;
    @FXML
    private TableColumn<Eveniment, String> locationColumn;
    @FXML
    private TableColumn<Eveniment, String> creatorColumn;
    @FXML
    private TableColumn<Eveniment, String> dateColumn;
    @FXML
    private TableView<Eveniment> tableView;

    public void setService(MainService service) {
        mainService = service;
        currentUser = mainService.findUser(mainService.getByUsername(mainService.getCurrentUser()).getId());
        initModel();
    }

    private void initModel() {
        model.setAll((Collection<? extends Eveniment>) mainService.findEvenimente());
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<Eveniment, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
        tableView.setItems(model);
    }


    public void handlecheckParticipate() {
        Eveniment eveniment = tableView.getSelectionModel().getSelectedItem();
        InvitationEveniment invitationEveniment = mainService.checkIfAccepted(currentUser, eveniment);
        if (!eveniment.equals(null)) {
            if (eveniment.getParticipants().contains(currentUser)) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("You already participate in this event!");
                a.showAndWait();
            }
            if (!eveniment.getParticipants().contains(currentUser)) {
                invitationEveniment.setReplyInvite("approved");
                mainService.updateInvitation(invitationEveniment);
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("You joined this event!");
                a.showAndWait();
            }

        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Please select an event!");
            a.showAndWait();
        }

    }


    public void handleSaveBtn() {
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        String location = txtLocation.getText();
        LocalDate localDate = eventDate.getValue();
        if (title != null && description != null && localDate != null && location != null) {
            mainService.createEveniment(new Eveniment(title, description, currentUser, localDate.atTime(15, 30), location));

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Event created succesfully!");
            a.showAndWait();
        }
    }
}
