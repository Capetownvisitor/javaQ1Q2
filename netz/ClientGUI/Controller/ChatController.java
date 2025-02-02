package netz.ClientGUI.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import netz.Channel;
import netz.ChannelStorage;
import netz.ClientGUI.ChatApplication;
import netz.Message;
import org.json.JSONArray;
import org.json.JSONException;

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
    ListView<Channel> channelList;

    Channel channel = null;

    ObservableList<Channel> channels = FXCollections.observableArrayList();

    // -----------------------------------------------------------------------------------------------------------------

    private void updateList() {
        channelList.setItems(channels);
    }

    private void initChannelList() throws JSONException {
        // Fill the Channel List

        JSONArray jsonArray = ChannelStorage.readChannels();

        for (int i = 0; i<jsonArray.length(); i++){
            this.channels.add(new Channel(jsonArray.getJSONObject(i)));
        }


        updateList();
        channelList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Handle channel change
                this.changeChannel(newValue);
            }
        });
    }

    private void changeChannel(Channel newChannel) {
        //System.out.println("Switched to channel: " + newChannel);
        this.channel = newChannel;
        // Update the main chat area with the selected channel's messages
        this.mainTextArea.clear();
        updateMessages();
    }

    public void initChat(String username, String pServerIP, int pServerPort) throws JSONException {
        this.usernameLabel.setText(username);
        this.serverIPLabel.setText("Server IP: " + pServerIP);
        this.serverPortLabel.setText("Server Port: " + String.valueOf(pServerPort));

        initChannelList();
    }

    public void updateMessages(){
        Platform.runLater(() -> {
            this.mainTextArea.clear();
            for (Message m: this.channel.getMessages()){
                this.mainTextArea.appendText(m.toString() + "\n");
            }
        });
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void receiveMessage(int channelID) {
        // if the message was recieved on the current channel, reload it.
        for (Channel i: this.channels){
            if (i.getChannelID() == channelID) {
                if (i.equals(this.channel)){
                    this.updateMessages();
                }
            }
        }
        updateMessages();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void onReconnect(ActionEvent event) {
        // handle the press of the reconnect button
        this.mainApp.reconnect();
    }

    public void onSend(ActionEvent event) {
        // handle the press of the Send button
        String stringBuilder;
        /*
        if (channel.getType() == Channel.ChannelType.PRIVATE){
            stringBuilder = "SEND" +
                    " " +
                    this.channel.getType().getPrefix() +
                    " " +
                    "[" +
                    this.channel.getChannelID() +
                    "]" +
                    " " +
                    this.channel.getUsers().getFirst() +
                    " " +
                    // append message
                    textField.getCharacters().toString();
        }else if (channel.getType() == Channel.ChannelType.GLOBAL) {
            stringBuilder = "SEND" +
                    " " +
                    this.channel.getType().getPrefix() +
                    " " +
                    "[" +
                    this.channel.getChannelID() +
                    "]" +
                    " " +
                    // append message
                    textField.getCharacters().toString();
        }else {
            // FIXME: Group Messages
            stringBuilder = "SEND" +
                    " " +
                    this.channel.getType().getPrefix() +
                    " " +
                    "[" +
                    this.channel.getChannelID() +
                    "]" +
                    " " +
                    // append message
                    textField.getCharacters().toString();
        }
         */

        stringBuilder = "SEND " + "[" + channel.getChannelID() + "]" + " " + textField.getCharacters().toString();

        System.out.println("DEBUG: sent message: " + stringBuilder);

        this.mainApp.send(stringBuilder);
        textField.clear();
        ChannelStorage.saveChannels(this.channelList);
        updateMessages();
    }

    // -----------------------------------------------------------------------------------------------------------------

    ChatApplication mainApp;

    public void addText(String message){
        this.mainTextArea.appendText(message + "\n");
    }

    public void setMainApp(ChatApplication mainApp){
        this.mainApp = mainApp;
    }
}
