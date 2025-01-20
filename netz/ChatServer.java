package netz;

import netz.Server;

import java.util.ArrayList;
import java.util.Collections;

public class ChatServer extends Server {

    //TODO: Start the server and connect with telnet: telnet localhost 4242
    // Quit from telnet with: Ctrl + 5, then type "quit" and press Enter

    // Attribute
    private ArrayList<User> userList;

    public ChatServer(int pPortNr)
    {
        super(pPortNr);
        userList = new ArrayList<User>();
        initUsers();
        System.out.println("ChatServer gestartet, läuft auf Port: " + pPortNr);
    }

    private void initUsers() {
        User u1 = new User("Jac", "123", "none", -1, -1);
        userList.add(u1);
        User u2 = new User("Jonass", "passwort", "none", -1, -1);
        userList.add(u2);
    }

    private User getUser(String pClientIP, int pClientPort) {
        for (User u: userList){
            if (u.getIpAddress().equals(pClientIP) && u.getPort() == pClientPort){
                return u;
            }
        }
        return null;
    }

    private void logoutUser(User user) {
        user.setState(0);
        user.setIpAddress("none");
        user.setPort(-1);
    }
    private void loginUser(User user, String pClientIP, int pClientPort) {
        user.setIpAddress(pClientIP);
        user.setPort(pClientPort);
        user.setState(1);
    }
    private void authenticateUser(User user) {
        user.setState(2);
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        this.send(pClientIP, pClientPort, "Willkommen im Chat!");
        System.out.println("Der Client mit der IP: " + pClientIP + " und dem Port: " + pClientPort + " hat sich verbunden.");
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        // Im folgenden wird die Nachricht verarbeitet nach dem Vorgegebenen Protokoll

        // Wenn die Nachricht leer ist, wird nichts gemacht
        if (pMessage.length() <= 0) return;

        // look up Online Users

        ArrayList<String> messageParts = new ArrayList<String>();
        Collections.addAll(messageParts, pMessage.split(" "));

        switch (messageParts.get(0).toUpperCase()){
            case "QUIT":
                // Check if user is currently logging in or was logged in.
                User quitUser = getUser(pClientIP, pClientPort);
                if (quitUser != null) {
                    // User was found and is being logged out
                    logoutUser(quitUser);
                }

                this.send(pClientIP, pClientPort, "+OK Auf Wiedersehen!");
                this.closeConnection(pClientIP, pClientPort);
                break;
            case "HELP":
                // Add checks if a command has been added after help
                this.send(pClientIP, pClientPort, "+OK Folgende Befehle sind verfügbar: ");
                this.send(pClientIP, pClientPort, "QUIT: Beendet die Verbindung");
                this.send(pClientIP, pClientPort, "HELP: Zeigt diese Hilfe an");
                break;
            case "LOGIN":
                // Check if the Credentials match any User
                if (messageParts.size() != 2){
                    this.send(pClientIP, pClientPort, "-ERR Wrong amount of Parameters!");
                    this.send(pClientIP, pClientPort, "Use: LOGIN <username>");
                }else {
                    // go through the List to find the User
                    User loginUser = null;
                    for (User u : userList) {
                        if (u.getNickname().equalsIgnoreCase(messageParts.get(1))) loginUser = u;
                    }
                    // Check if a user was found
                    if (loginUser == null){
                        this.send(pClientIP, pClientPort, "-ERR User does not Exist");
                    }else {
                        // check if the user is already logged in:

                        if (loginUser.getState() > 0)
                        {
                            // optionally tell the User what they should do right now
                            if (loginUser.getState() == 1) this.send(pClientIP, pClientPort, "-ERR Can't Login, Please provide the Password");
                            else this.send(pClientIP, pClientPort, "-ERR Cant Login, already logged in");
                        }else {
                            // Take User one Step further to now require Password, and save the IP and Port to the USer Object
                            loginUser(loginUser, pClientIP, pClientPort);
                            this.send(pClientIP, pClientPort, "+OK Please Provide the Password for " + loginUser.getNickname());
                        }
                    }
                }

                break;
            case "PASS":
                User passUser = getUser(pClientIP, pClientPort);
                if (passUser == null) {
                    send(pClientIP, pClientPort, "-ERR User not found");
                    break;
                }else {
                    if (messageParts.size() != 2){
                        this.send(pClientIP, pClientPort, "-ERR Wrong amount of Parameters!");
                        this.send(pClientIP, pClientPort, "Use: PASS <password>");
                    }else {
                        // Check if the Password matches the User
                        if (messageParts.get(1).equals(passUser.getPassword())) {
                            this.send(pClientIP, pClientPort, "+OK You have been authenticated and are Logged in!");
                            authenticateUser(passUser);
                        }else {
                            this.send(pClientIP, pClientPort, "-ERR Password Incorrect.");
                            this.send(pClientIP, pClientPort, "Try again or Log out with: LOGOUT");
                        }
                    }
                }
                break;
            case "LOGOUT":
                // Log the USer out
                User logoutUser = getUser(pClientIP, pClientPort);
                if (logoutUser != null) {
                    send(pClientIP, pClientPort, "+OK User Logged out.");
                    logoutUser(logoutUser);
                    break;
                }else {
                    send(pClientIP, pClientPort, "-ERR User not found.");
                }
                break;
            case "ADD":
                // Lets the User "Subscribe" to different Groups.
                // If the Group has a Password ask for the password to join the Group
                this.send(pClientIP, pClientPort, "-ERR Not Implemented yet!");
                break;
            case "SEND":
                // Send a message to a recipient.
                // Can be a single Person
                // Can be a specified Group, the User belongs to
                this.send(pClientIP, pClientPort, "-ERR Not Implemented yet!");
                break;
            case "ARCHIVE":
                // Check for the Permissions of the User if they may read the messages
                this.send(pClientIP, pClientPort, "-ERR Not Implemented yet!");
                break;
            default:
                this.send(pClientIP, pClientPort, "-ERR Unbekannter Befehl: " + messageParts.get(0));
                this.send(pClientIP, pClientPort, "Folgende Befehle sind verfügbar: ");
                this.send(pClientIP, pClientPort, "quit: Beendet die Verbindung");
                this.send(pClientIP, pClientPort, "help: Zeigt diese Hilfe an");
                break;
        }
    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {

    }

    public static void main(String[] args)  {
        ChatServer cs = new ChatServer(4242);
    }

}
