package com.example.proiectjavafx;

import domain.DTOchat;
import domain.Message;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import obs.Observer;
import service.MainService;

import java.util.ArrayList;
import java.util.List;

public class ChatController extends Observer {
    private MainService service;
    private final ObservableList<DTOchat> model = FXCollections.observableArrayList();
    private String fromUser;

    @FXML
    private TableColumn<DTOchat, String> whoColumn;

    @FXML
    private TableColumn<DTOchat, String> textColumn;

    @FXML
    private TableView<DTOchat> tableView;

    @FXML
    private TextArea txtMessage;

    public void setService(MainService service, String fromUser) {
        this.fromUser = fromUser;
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }


    private void initModel() {
        List<DTOchat> chats = service.getChats(service.getByUsername(service.getCurrentUser()).getId(), service.getByUsername(fromUser).getId());
        model.setAll(chats);
    }

    @FXML
    public void initialize() {
        whoColumn.setCellValueFactory(new PropertyValueFactory<>("userFrom"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        tableView.setItems(model);


    }


    public void handleBtnReply() {
        DTOchat selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (!txtMessage.getText().isEmpty()) {
                List<User> l = new ArrayList<>();
                l.add(service.getByUsername(fromUser));
                service.saveMsg(new Message(service.getByUsername(service.getCurrentUser()), l, txtMessage.getText(), service.findMsg(selected.getId())));

            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You must type something!");
                a.showAndWait();
            }
        }
    }

    public void handleBtnSend() {
        if (!txtMessage.getText().isEmpty()) {
            List<User> l = new ArrayList<>();
            l.add(service.getByUsername(fromUser));
            service.saveMsg(new Message(service.getByUsername(service.getCurrentUser()), l, txtMessage.getText(), null));


        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("You must type something!");
            a.showAndWait();
        }

    }

    public void handleBtnReplyAll() {
        DTOchat selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (!txtMessage.getText().isEmpty()) {
                List<User> l = service.findMsg(selected.getId()).getTo();
                l.remove(service.getByUsername(service.getCurrentUser()));
                l.add(service.findMsg(selected.getId()).getFrom());
                service.saveMsg(new Message(service.getByUsername(service.getCurrentUser()), l, txtMessage.getText(), service.findMsg(selected.getId())));

            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You must type something!");
                a.showAndWait();
            }
        }
    }

    public void handleBtnDelete(ActionEvent actionEvent) {
    }

    public void handleBtnRefresh(ActionEvent actionEvent) {
        initModel();
    }

    @Override
    protected void update() {
        initModel();
    }
}
