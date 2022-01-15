package com.example.proiectjavafx;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.MainService;
import service.savePDF.ReportPDF;

import java.time.LocalDate;

public class ReportPDFController {
    private final ObservableList<User> model = FXCollections.observableArrayList();
    private MainService mainService;
    private ReportPDF reportPDF;
    private User currentUser;



    public void setService(MainService service, ChoiceBox friendSelection) {
        mainService = service;
        reportPDF = new ReportPDF(mainService);
        currentUser = mainService.getByUsername(mainService.getCurrentUser());
        initModel(friendSelection);
    }

    public void initModel(ChoiceBox friendSelection) {
        model.setAll(mainService.getUserFriends(currentUser.getId()));
        friendSelection.setItems(model);
    }


    public void handleBtnActivitiesReports(LocalDate firstDate, LocalDate secondDate) {
        reportPDF.createPDFforActivities(currentUser, firstDate, secondDate);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Report saved successfully!");
        a.showAndWait();
    }

    public void handleBtnMessagesReports(LocalDate firstDate, LocalDate secondDate, User userFriend) {
        if (!userFriend.equals(null)) {
            reportPDF.createPDFforMessages(currentUser, userFriend, firstDate, secondDate);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Report saved successfully!");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select a friend!");
            a.showAndWait();
        }
    }
}