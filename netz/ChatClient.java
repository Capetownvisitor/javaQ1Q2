package netz;

import netz.ClientGUI.ChatApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ChatClient extends Client{

    private ChatApplication app;
    private boolean isLoggedIn = false;

    public ChatClient(String pServerIP, int pServerPort, ChatApplication app) throws IOException {
        super(pServerIP, pServerPort);
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

        switch (messageParts.getFirst()) {
            case "-ERR":
                // handle the Error
                if (messageParts.size() < 2) {
                    System.out.println("Server response not informational enough: " + pMessage);
                    return;
                }
                switch (messageParts.get(1)){
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
                switch (messageParts.get(1)){
                    case "200":
                        // OK
                        break;
                    case "201":
                        // OK logged in
                        System.out.println("DEBUG: LOG IN WAS SUCCESSFULL");
                        this.setLoggedIn(true);
                        //app.loggedIn = true;
                        app.confirmLogin();
                        break;
                    default:
                        break;
                }
                // handle the Okay
                break;
            default:
                System.out.println("Could not comprehend what the Server was sending: " + pMessage);
                break;
        }

    }

    /*
        TODO:
        - Add functionality by building methods in the Client
        - Add the functionality to Login
        - Add the functionality to Leave a server
        - Add the functionality to reconnect to a Server
        - Add the functionality to Send a message -> Enum over all the types of recipients ?
     */

    public void login(String username, String password) {
        System.out.println("DEBUG: Sending login: username: " + username + " password: " + password);
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
}
