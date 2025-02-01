package netz.ClientGUI.Controller;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import netz.Channel;
import netz.ChannelSaver;
import netz.ClientGUI.ChatApplication;
import netz.Message;
import org.json.JSONArray;
import org.json.JSONException;

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
    ListView<Channel> channelList;

    Channel channel = null;

    ObservableList<Channel> channels = FXCollections.observableArrayList();

    // -----------------------------------------------------------------------------------------------------------------

    private void updateList() {
        channelList.setItems(channels);
    }

    private void initChannelList() throws JSONException {
        // Fill the Channel List
        /*
        Channel c1 = new Channel(0, "General", Channel.ChannelType.GLOBAL);
        Channel c2 = new Channel(1, "Jackpie", Channel.ChannelType.PRIVATE);
        Channel c3 = new Channel(2, "Group", Channel.ChannelType.GROUP);

        c2.addAddresse("Jackpie");

        c3.addAddresse("Jackpie");
        c3.addAddresse("Jonass");

        channels.add(c1);
        channels.add(c2);
        channels.add(c3);
         */

        JSONArray jsonArray = ChannelSaver.readChannels();

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
        // TODO: Add all texts of the new Channel to Text area
        updateMessages();
    }

    public void initChat(String username, String pServerIP, int pServerPort) throws JSONException {
        this.usernameLabel.setText(username);
        this.serverIPLabel.setText("Server IP: " + pServerIP);
        this.serverPortLabel.setText("Server Port: " + String.valueOf(pServerPort));

        initChannelList();
    }

    public void updateMessages(){
        this.mainTextArea.clear();
        for (Message m: this.channel.getMessages()){
            this.mainTextArea.appendText(m.toString() + "\n");
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void receiveMessage(Message message, int channelID) {
        // Add the Message to the right channel
        for (Channel i: this.channels){
            if (i.getChannelID() == channelID) {
                i.addMessage(message);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void onReconnect(ActionEvent event) {
        // handle the press of the reconnect button
        this.mainApp.reconnect();
    }

    public void onSend(ActionEvent event) {
        // handle the press of the Send button
        String stringBuilder;
        // FIXME: Optimize pls
        if (channel.getType() == Channel.ChannelType.PRIVATE){
            stringBuilder = "SEND" +
                    " " +
                    this.channel.getType().getPrefix() +
                    " " +
                    "[" +
                    this.channel.getChannelID() +
                    "]" +
                    " " +
                    this.channel.getAddresses().getFirst() +
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
        this.mainApp.send(stringBuilder);
        textField.clear();
        ChannelSaver.saveChannels(this.channelList);
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
