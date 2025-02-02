package netz;

import netz.ClientGUI.ChatApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ChatClient extends Client{

    private ChatApplication app;
    private boolean isLoggedIn = false;
    private boolean isReadingChat = false;

    private String nickname;
    private String serverIP;
    private int serverPort;

    public ChatClient(String pServerIP, int pServerPort, ChatApplication app) throws IOException {
        super(pServerIP, pServerPort);
        this.serverIP = pServerIP;
        this.serverPort = pServerPort;
        this.app = app;
    }

    @Override
    public void processMessage(String pMessage) {
        // Process all the Messages that you could get Back from the Server

        // Split the message into parts

        String[] messages = pMessage.split(" ");
        ArrayList<String> messageParts = new ArrayList<String>();
        Collections.addAll(messageParts, messages);

        // message must contain commands / Information
        if (messageParts.isEmpty()) {
            return;
        }

        if (!isReadingChat) {
            switch (messageParts.getFirst()) {
                case "-ERR":
                    // handle the Error
                    if (messageParts.size() < 2) {
                        System.out.println("Server response not informational enough: " + pMessage);
                        return;
                    }
                    switch (messageParts.get(1)) {
                        case "400":
                            //info wrong amount of parameters
                            break;
                        case "401":
                            //info incorrect parameters
                            break;
                        case "402":
                            //info not logged in
                            break;
                        case "403":
                            //info no permission
                            break;
                        case "404":
                            //info command unknown
                            break;
                        case "405":
                            //info user not online
                            break;
                        case "406":
                            //info user must be logged out
                            break;
                        case "500":
                            //info Not implemented yet
                            break;
                        default:
                            // No error code provided
                            break;
                    }
                    break;
                case "+OK":
                    if (messageParts.size() < 2) {
                        System.out.println("Server response not informational enough: " + pMessage);
                        return;
                    }
                    switch (messageParts.get(1)) {
                        case "200":
                            // OK connected to the Server
                            app.confirmConnection();
                            break;
                        case "201":
                            // OK logged in
                            if (messageParts.size() < 3) {
                                System.out.println("Server response not informational enough, missing name: " + pMessage);
                                return;
                            }
                            String nickname = messageParts.get(2);
                            this.nickname = nickname;
                            this.setLoggedIn(true);
                            //app.loggedIn = true;
                            app.confirmLogin();
                            break;
                        case "202":
                            // OK Message recieved -> Reload the given chat
                            if (messageParts.size() < 3) {
                                System.out.println("Server response not informational enough, missing channel id: " + pMessage);
                                return;
                            }
                            System.out.println("DEBUG: Message recieved: " + pMessage);
                            // reload the channel
                            try {
                                int id = Integer.parseInt(NameExtractor.extractChannelID(messageParts.get(2)));
                                app.recieveMessage(id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        default:
                            break;
                    }
                    // handle the Okay
                    break;
                default:
                    // check if it was a Message
                    /*
                    if (messageParts.size() < 3) {
                        System.out.println("Message too short, Could not comprehend what the Server was sending: " + pMessage);
                        return;
                    }


                    String namePart = messageParts.get(1);

                    if (namePart.startsWith("<") && namePart.endsWith(">:")) {
                        // Info: get Channel -> [channelID] <nickname>: message
                        try {

                            // Get the name from the message
                            String senderName = NameExtractor.extractName(namePart);

                            // Get the Message from the Text
                            StringBuilder text = new StringBuilder();
                            for (int i = 2; i<messageParts.size();i++){
                                text.append(messageParts.get(i) + " ");
                            }
                            Message message = new Message(senderName, text.toString());

                            // Get the channel id from the message
                            String channelIDString = NameExtractor.extractChannelID(messageParts.getFirst());
                            int channelID = Integer.parseInt(channelIDString);

                            app.recieveMessage(message, channelID);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("Could not comprehend what the Server was sending: " + pMessage);
                    }

                     */
                    System.out.println("Could not comprehend what the Server was sending: " + pMessage);
                    break;
            }
        }
        else {
            if (pMessage.equalsIgnoreCase(".")){
                // message has ended
                this.isReadingChat = false;
                return;
            }else {
                // get the message line by line from the Server
            }
        }
    }

    public void login(String username, String password) {
        this.send("LOGIN " + username + " " + password);
    }

    public void logout() {
        this.send("LOGOUT");
    }

    public void quit() {
        this.send("QUIT");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getNickname() {
        return nickname;
    }

}
