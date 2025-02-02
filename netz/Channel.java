package netz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Channel {


    // TODO: add functionality to the Channel Class
    public enum ChannelType {
        GLOBAL ("@all"),
        PRIVATE ("@pm"),
        GROUP ("@gm");

        private final String prefix;

        ChannelType(String s) {
            this.prefix = s;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private final int channelID;
    private String channelName;
    private ChannelType channelType;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Message> messages = new ArrayList<Message>();

    public Channel(int channelID, String channelName, ChannelType channeltype) throws JSONException {
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelType = channeltype;
    }

    public Channel(JSONObject jsonObject) throws JSONException {
        this.channelID = jsonObject.getInt("id");
        this.channelName = jsonObject.getString("name");
        this.channelType = ChannelType.valueOf(jsonObject.getString("type"));

        JSONArray userArray = jsonObject.getJSONArray("users");
        JSONArray messagesArray = jsonObject.getJSONArray("messages");

        for (int i = 0; i<userArray.length();i++){
            this.users.add(new User(userArray.getJSONObject(i)));
        }

        for (int i = 0; i<messagesArray.length();i++){
            this.messages.add(new Message(messagesArray.getJSONObject(i)));
        }
    }

    public void addMessage(Message m){
        this.messages.add(m);
    }

    public void addUser(User u) {
        this.users.add(u);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public int getChannelID() {
        return channelID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ChannelType getType() {
        return channelType;
    }

    public void setType(ChannelType channelType) {
        this.channelType = channelType;
    }

    @Override
    public String toString() {
        return this.channelName;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray userArray = new JSONArray();
        JSONArray messageArray = new JSONArray();

        for (User u: this.getUsers()){
            userArray.put(u.toJson());
        }

        for (Message message: this.getMessages()){
            messageArray.put(message.toJson());
        }

        jsonObject.put("id", this.getChannelID()); // Channel ID
        jsonObject.put("name", this.getChannelName()); // Channel Name
        jsonObject.put("type", this.getType()); // Channel Type
        jsonObject.put("users", userArray); // Address of Channel -> names for private or Group
        jsonObject.put("messages", messageArray); // List of messages
        return jsonObject;
    }

}
