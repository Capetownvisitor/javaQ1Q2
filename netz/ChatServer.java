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
                this.send(pClientIP, pClientPort, "Auf Wiedersehen!");
                this.closeConnection(pClientIP, pClientPort);
                break;
            case "HELP":
                // Add checks if a command has been added after help
                this.send(pClientIP, pClientPort, "Folgende Befehle sind verfügbar: ");
                this.send(pClientIP, pClientPort, "QUIT: Beendet die Verbindung");
                this.send(pClientIP, pClientPort, "HELP: Zeigt diese Hilfe an");
                break;
            case "LOGIN":
                // Check if the Credentials match any User
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            case "SIGNUP":
                // If valid arguments, add user to the Database and log them in
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            case "LOGOUT":
                // Log the USer out
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            case "ADD":
                // Lets the User "Subscribe" to different Groups.
                // If the Group has a Password ask for the password to join the Group
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            case "SEND":
                // Send a message to a recipient.
                // Can be a single Person
                // Can be a specified Group, the User belongs to
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            case "ARCHIVE":
                // Check for the Permissions of the User if they may read the messages
                this.send(pClientIP, pClientPort, "Not Implemented yet!");
                break;
            default:
                this.send(pClientIP, pClientPort, "Unbekannter Befehl: " + messageParts.get(0));
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
