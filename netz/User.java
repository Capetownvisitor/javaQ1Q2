package netz;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    // Attributes
    private int id;
    private String nickname;
    private String pw;
    private String ipAddress;
    private int port;
    private int state;
    private ArrayList<String> permissions;

    // Constructor
    public User(String nickname, String pw, String ipAddress, int port, int state) {
        this.nickname = nickname;
        this.pw = pw;
        this.ipAddress = ipAddress;
        this.port = port;
        this.state = state;
    }

    public User(String nickname, String pw, String ipAddress, int port, int state, ArrayList<String> permissions) {
        this.nickname = nickname;
        this.pw = pw;
        this.ipAddress = ipAddress;
        this.port = port;
        this.state = state;
        this.permissions = permissions;
    }

    public User(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.nickname = jsonObject.getString("nickname");
        this.pw = jsonObject.getString("password");
        this.ipAddress = jsonObject.getString("ipAddress");
        this.port = jsonObject.getInt("port");
        this.state = jsonObject.getInt("state");
        // TODO: Add permissions
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.getId());
        jsonObject.put("nickname", this.getNickname());
        jsonObject.put("password", this.getPassword());
        jsonObject.put("ipAddress", this.getIpAddress());
        jsonObject.put("port", this.getPort());
        jsonObject.put("state", this.getState());
        // TODO: Add permissions

        return jsonObject;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return pw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
