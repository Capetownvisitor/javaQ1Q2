package netz;

import org.json.JSONException;
import org.json.JSONObject;

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
        UserStorage.saveUsers(userList);
        System.out.println("ChatServer started, runnign on Port: " + pPortNr);
    }

    private void initUsers() {
        User u1 = new User("Jackpie", "123", "none", -1, -1);
        userList.add(u1);
        User u2 = new User("Jonass", "passwort", "none", -1, -1);
        userList.add(u2);
    }

    private User getConnectedUser(String pClientIP, int pClientPort){

        JSONObject userObject = UserStorage.getUserByConnection(pClientIP, pClientPort);
        if (userObject != null) {
            try {
                return new User(userObject);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        /*
        for (User u: userList){
            if (u.getIpAddress().equals(pClientIP) && u.getPort() == pClientPort){
                return u;
            }
        }

        */
        return null;
    }
    private User getUserByName(String pUsername) {

        JSONObject userObject = UserStorage.getUserByName(pUsername);
        if (userObject != null) {
            try {
                return new User(userObject);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }
        /*
        User result = null;
        for (User user: userList) {
            if (user.getNickname().equalsIgnoreCase(pUsername)) {
                result = user;
            }
        }
        return result;

         */
    }

    private void logoutUser(User user) {
        user.setState(0);
        user.setIpAddress("none");
        user.setPort(-1);
    }
    private void loginUser(User user, String pClientIP, int pClientPort, int pState) {
        user.setIpAddress(pClientIP);
        user.setPort(pClientPort);
        user.setState(pState);
    }
    private void authenticateUser(User user) {
        user.setState(2);
    }


    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        this.send(pClientIP, pClientPort, ANSIColors.GREEN);
        this.send(pClientIP, pClientPort, "+OK 200 Welcome to the Chat!");
        this.send(pClientIP, pClientPort, "Please Log in to communicate!" + ANSIColors.RESET);
        System.out.println("The Client with the IP: " + pClientIP + " and the Port: " + pClientPort + " has connected.");
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        // Process Message as per the Rules set by the Protocol
        User user = getConnectedUser(pClientIP, pClientPort);
        // If message is empty Return
        if (pMessage.length() <= 0) return;
        // Split the input into parts so that you can work through it
        ArrayList<String> messageParts = new ArrayList<String>();
        Collections.addAll(messageParts, pMessage.split(" "));

        switch (messageParts.get(0).toUpperCase()){
            case "QUIT":
                // Check if user is currently logging in or was logged in.
                if (user != null) {
                    // User was found and is being logged out
                    logoutUser(user);
                }

                this.send(pClientIP, pClientPort, "+OK Bye!");
                this.closeConnection(pClientIP, pClientPort);
                break;
            case "HELP":
                // Add checks if a command has been added after help
                this.send(pClientIP, pClientPort, "+OK Supported commands are: ");
                this.send(pClientIP, pClientPort, "QUIT: quit the connection.");
                this.send(pClientIP, pClientPort, "HELP: shows this help.");
                this.send(pClientIP, pClientPort, "LOGIN <username>: Log in as this user.");
                this.send(pClientIP, pClientPort, "PASS <password>: Add password if asked to do so.");
                this.send(pClientIP, pClientPort, "SEND <@gm / @pm / @all> < groupname / username> <message>: Send message to Groups or users");
                this.send(pClientIP, pClientPort, "LOGOUT: log out.");
                this.send(pClientIP, pClientPort, "ADD <@g / @p> < group-Password >: Add users or groups to your chats");
                this.send(pClientIP, pClientPort, "ARCHIVE: Currently unsupported");
                break;
            case "LOGIN":
                // Check if the Credentials match any User
                if (messageParts.size() < 2){
                    this.send(pClientIP, pClientPort, "-ERR 400 Wrong amount of Parameters!");
                    this.send(pClientIP, pClientPort, "Use: LOGIN <username>");
                    this.send(pClientIP, pClientPort, "Or: LOGIN <username> <password>");
                }if( messageParts.size() == 2 ) {
                    // go through the List to find the User
                    for (User u : userList) {
                        if (u.getNickname().equalsIgnoreCase(messageParts.get(1))) user = u;
                    }
                    // Check if a user was found
                    if (user == null){
                        this.send(pClientIP, pClientPort, "-ERR 401 User does not Exist");
                    }else {
                        // check if the user is already logged in:

                        if (user.getState() > 0)
                        {
                            // optionally tell the User what they should do right now
                            if (user.getState() == 1) this.send(pClientIP, pClientPort, "-ERR Can't Login, Please provide the Password");
                            else this.send(pClientIP, pClientPort, "-ERR 406 Cant Login, already logged in");
                        }else {
                            // Take User one Step further to now require Password, and save the IP and Port to the USer Object
                            loginUser(user, pClientIP, pClientPort, 1);
                            this.send(pClientIP, pClientPort, "+OK 203 Please Provide the Password for " + user.getNickname());
                        }
                    }
                }else if (messageParts.size() == 3){
                    if (user != null && user.getState() > 0) {
                        this.send(pClientIP, pClientPort, "-ERR 406 You must be logged out to log in.");
                    }
                    User loginUser = null;
                    for (User u: userList){
                        if (u.getNickname().equalsIgnoreCase(messageParts.get(1)) && u.getPassword().equalsIgnoreCase(messageParts.get(2))){
                            // auth user
                            this.loginUser(u, pClientIP, pClientPort, 2);
                            loginUser = u;
                            //loginUser.setState(2);
                            this.send(pClientIP, pClientPort, "+OK 201 " + u.getNickname() + " You have been authenticated and are Logged in!");
                        }
                    }
                    if (loginUser == null){
                        this.send(pClientIP, pClientPort, "-ERR 401 USER NOT FOUND");
                    }
                }else {
                    this.send(pClientIP, pClientPort, "-ERR 400 Wrong amount of Parameters!");
                    this.send(pClientIP, pClientPort, "Use: LOGIN <username>");
                    this.send(pClientIP, pClientPort, "Or: LOGIN <username> <password>");
                }
                break;
            case "PASS":
                if (user == null) {
                    send(pClientIP, pClientPort, "-ERR 401 User not found");
                    break;
                }else {
                    if (user.getState() != 1) {
                        this.send(pClientIP, pClientPort, "-ERR Supply a username first;");
                        break;
                    }
                    if (messageParts.size() != 2){
                        this.send(pClientIP, pClientPort, "-ERR 400 Wrong amount of Parameters!");
                        this.send(pClientIP, pClientPort, "Use: PASS <password>");
                    }else {
                        // Check if the Password matches the User
                        if (messageParts.get(1).equals(user.getPassword())) {
                            this.send(pClientIP, pClientPort, "+OK 201 You have been authenticated and are Logged in!");
                            authenticateUser(user);
                        }else {
                            this.send(pClientIP, pClientPort, "-ERR Password Incorrect.");
                            this.send(pClientIP, pClientPort, "Try again or Log out with: LOGOUT");
                        }
                    }
                }
                break;
            case "LOGOUT":
                // Log the USer out
                if (user != null) {
                    send(pClientIP, pClientPort, "+OK User Logged out.");
                    logoutUser(user);
                    break;
                }else {
                    send(pClientIP, pClientPort, "-ERR 402 Not logged in.");
                }
                break;
            case "ADD":
                // Lets the User "Subscribe" to different Groups.
                // If the Group has a Password ask for the password to join the Group
                this.send(pClientIP, pClientPort, "-ERR 500 Not Implemented yet!");
                break;
            case "SEND":
                // Check if User is Connected / Logged In
                if (user == null || user.getState() < 2) {
                    this.send(pClientIP, pClientPort, "-ERR 402 You have to be logged in to use this feature.");
                    break;
                }
                // Check if Command is long enough
                if (messageParts.size() < 3){
                    this.send(pClientIP, pClientPort, "-ERR 400 Usage: SEND <[channelID]> <message>");
                    break;
                }

                // Prefix
                //String prefix = "<" + sendUser.getNickname() + ">:" + " ";

                String channelID = messageParts.get(2) + " ";

                // INFO: New way of sending messages
                // info => SEND <[channelID]> <message>
                if (messageParts.size() < 3) {
                    this.send(pClientIP, pClientPort, "-ERR 400 Not Enough Parameters. // Usage => SEND <[channelID]> <message> ");
                    return;
                }

                System.out.println("DEBUG: Message recieved: " + pMessage);

                try {
                    String idString = NameExtractor.extractChannelID(channelID);
                    int id = Integer.parseInt(idString);

                    // build message
                    StringBuilder privateMsg = new StringBuilder();
                    for (int i = 3; i < messageParts.size(); i++){
                        privateMsg.append(messageParts.get(i)).append(" ");
                    }

                    Message message = new Message(user.getNickname(), privateMsg.toString());

                    // Add the Message to the "Database"
                    ChannelStorage.addMessage(message, id);

                    // Now send the message to all recipients
                    JSONObject channelJson = ChannelStorage.retrieveChannel(id);
                    assert channelJson != null;
                    Channel c = new Channel(channelJson);

                    if (c.getType() == Channel.ChannelType.GLOBAL) {
                        this.sendToAll("+OK " + channelID + "Message recieved. Reload on client.");
                    }else {
                        for (User u: c.getUsers()){
                            this.send(u.getIpAddress(), u.getPort(), "+OK " + channelID + "Message recieved. Reload on client.");
                        }
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }


                // Go through types of messages
                /*
                switch (messageParts.get(1).toUpperCase()) {
                    case "@GM":
                        this.send(pClientIP, pClientPort, "-ERR 500 Currently not Supported.");
                        break;
                    case "@PM":

                        if (messageParts.size() < 5) {
                            this.send(pClientIP, pClientPort, "-ERR 400 Not Enough Parameters.");
                            this.send(pClientIP, pClientPort, "Usage: SEND <@gm / @pm / @all> <[channelID]> < groupname / username> <message>");
                            break;
                        }
                        // Check if a username was added
                        User fromUser = getConnectedUser(pClientIP, pClientPort);
                        User toUser = null;
                        String toUsername = messageParts.get(3);
                        // check if the user exists
                        toUser = getUserByName(toUsername);
                        if (toUser == null) {
                            this.send(pClientIP, pClientPort, "-ERR 401 User not found");
                            break;
                        }
                        // check if the user is online
                        if (toUser.getIpAddress().equalsIgnoreCase("none") || toUser.getPort() < 0) {
                            this.send(pClientIP, pClientPort, "-ERR 405 User is not online.");
                            break;
                        }
                        // send message

                        // build message
                        StringBuilder privateMsg = new StringBuilder();
                        for (int i = 3; i < messageParts.size(); i++){
                            privateMsg.append(messageParts.get(i)).append(" ");
                        }
                        this.send(toUser.getIpAddress(), toUser.getPort(),channelID + prefix + privateMsg.toString());


                        break;
                    case "@ALL":
                        StringBuilder allMsg = new StringBuilder();
                        for (int i = 3; i < messageParts.size(); i++){
                            allMsg.append(messageParts.get(i)).append(" ");
                        }
                        this.sendToAll(channelID + prefix + allMsg.toString());

                        break;
                    default:
                        send(pClientIP, pClientPort, "-ERR 401 Usage: SEND <@gm / @pm / @all> < groupname / username / > <message>");
                        break;
                }
                */

                break;
            case "ARCHIVE":
                // TODO: Check for the Permissions of the User if they may read the messages
                // Check what chat they want to read -> Channel id
                //
                if (messageParts.size() < 2){
                    this.send(pClientIP, pClientPort, "-ERR 400 Usage: ARCHIVE <channelID>");
                    return;
                }

                // check for permissions

                // TODO: query the "Databse" -> Json file, for the channel

                this.send(pClientIP, pClientPort, "-ERR 500 Not Implemented yet!");
                break;
            default:
                this.send(pClientIP, pClientPort, "-ERR 404 Unbekannter Befehl: " + messageParts.getFirst() + "\n" +
                        "Folgende Befehle sind verfügbar: \n" +
                        "quit: Ends connection\n" +
                        "help: shows this help\n");
                break;
        }
    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        User quitUser = getConnectedUser(pClientIP, pClientPort);
        if (quitUser != null) {
            // User was found and is being logged out
            logoutUser(quitUser);
        }
        System.out.println("The Client with the IP: " + pClientIP + " and the Port: " + pClientPort + " has left the Server.");
    }

    public static void main(String[] args)  {
        ChatServer cs = new ChatServer(4242);
    }

}
