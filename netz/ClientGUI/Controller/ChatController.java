package netz.ClientGUI.Controller;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import netz.ClientGUI.ChatApplication;

import java.util.*;

public class ChatController {

    @FXML
    TextArea mainTextArea;

    @FXML
    TextField textField;

    @FXML
    Label serverIPLabel;

    @FXML
    Label serverPortLabel;

    @FXML
    Label usernameLabel;

    @FXML
    ComboBox<String> methodComboBox;

    // TODO:
    // Add functionality to dynamically populate List
    // Add the listener to change the Current Channel the user is writing in

    @FXML
    ListView<String> channelListView;

    ObservableList<String> channels = FXCollections.observableArrayList("General", "Jonass", "Jackpie");

    private void updateList() {
        channelListView.setItems(channels);
    }

    private void initComboBox() {
        ArrayList<String> combinations = new ArrayList<String>();
        combinations.add("SEND");
    }


    public void initChat(String username, String pServerIP, int pServerPort) {
        this.usernameLabel.setText(username);
        this.serverIPLabel.setText("Server IP: " + pServerIP);
        this.serverPortLabel.setText("Server Port: " + String.valueOf(pServerPort));
    }

    public void onReconnect(ActionEvent event) {
        // handle the press of the reconnect button
        this.mainApp.reconnect();
    }

    public void onSend(ActionEvent event) {
        // handle the press of the Send button
    }

    ChatApplication mainApp;

    public void setMainApp(ChatApplication mainApp){
        this.mainApp = mainApp;
    }
}
