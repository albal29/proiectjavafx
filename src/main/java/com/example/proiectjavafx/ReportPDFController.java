package com.example.proiectjavafx;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import service.MainService;
import service.savePDF.ReportPDF;

import java.time.LocalDate;

public class ReportPDFController {
    private final ObservableList<User> model = FXCollections.observableArrayList();
    private MainService mainService;
    private ReportPDF reportPDF;
    private User currentUser;

    @FXML
    private Button activitiesBtn;
    @FXML
    private Button messagesBtn;

    @FXML
    private DatePicker firstDate;
    @FXML
    private DatePicker secondDate;
    @FXML
    private ChoiceBox<User> friendSelection;

    public void setService(MainService service) {
        mainService = service;
        reportPDF = new ReportPDF(mainService);
        currentUser = mainService.getByUsername(mainService.getCurrentUser());
        initModel();
    }

    private void initModel() {
        model.setAll(mainService.getUserFriends(currentUser.getId()));
        friendSelection.setItems(model);
    }

    public LocalDate handleFirstDate() {
        return firstDate.getValue();
    }

    public LocalDate handleSecondDate() {
        return secondDate.getValue();
    }

    public void handleBtnActivitiesReports() {
        reportPDF.createPDFforActivities(currentUser, handleFirstDate(), handleSecondDate());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Report saved successfully!");
        a.showAndWait();
    }

    public void handleBtnMessagesReports() {
        User userFriend = friendSelection.getSelectionModel().getSelectedItem();
        if (!userFriend.equals(null)) {
            reportPDF.createPDFforMessages(currentUser, userFriend, handleFirstDate(), handleSecondDate());
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Report saved successfully!");
            a.showAndWait();
        } else {
            messagesBtn.setDisable(true);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend!");
            a.showAndWait();
        }
    }
}