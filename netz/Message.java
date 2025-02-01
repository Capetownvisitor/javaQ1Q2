package netz;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class Message  implements Serializable {
    private final Date date;
    private final String messageBody;
    private final String sender;

    public Message(String sender, String text){
        this.sender = sender;
        this.date = Date.from(Instant.now());
        this.messageBody = text;
    }

    public Message(JSONObject jsonObject) throws JSONException {
        this.sender = jsonObject.getString("sender");
        this.date = new Date(jsonObject.getLong("date"));
        this.messageBody = jsonObject.getString("body");
    }

    public long getDateAsLong() {
        return date.getTime();
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSender() {
        return sender;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender", this.getSender());
        jsonObject.put("date", this.getDateAsLong());
        jsonObject.put("body", this.getMessageBody());
        return jsonObject;
    }

    @Override
    public String toString() {
        return this.date.toString() +
                " <" +
                this.sender +
                ">: " +
                this.messageBody;
    }
}
