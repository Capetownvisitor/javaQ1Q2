package netz;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ChannelStorage {

    private static final String filePath = "netz/storage/channels.json";

    public static void saveChannels(ListView<Channel> channels) {
        File file = new File(filePath);

        try (FileWriter fileWriter = new FileWriter(file, false)) { // 'false' clears file before writing
            JSONArray jsonArray = new JSONArray();
            for (Channel channel : channels.getItems()) {
                jsonArray.put(channel.toJson());
            }
            fileWriter.write(jsonArray.toString(4)); // Pretty print with indentation
            fileWriter.flush();
        } catch (IOException | JSONException e) {
            e.printStackTrace(); // Handle exception properly in production code
        }
    }

    public static JSONArray readChannels(){
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return new JSONArray(); // Return empty object if file doesn't exist
        }

        try ( FileReader fileReader = new FileReader(file)) {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            return new JSONArray(content);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static void addChannel(Channel channel) {

    }

    public static void addMessage(Message message, int channelID) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }


        try {
            //System.out.println("1 DEBUG!!!: " + Files.readString(file.toPath()));
            String content = Files.readString(file.toPath());

            try (FileWriter fileWriter = new FileWriter(file, false)) {

                // FIXME: Someohow whenever trying to read the json file here,

                JSONArray arr = new JSONArray(content);

                for (int i = 0; i<arr.length();i++){
                    if (((JSONObject) arr.get(i)).getInt("id") == channelID) {
                        // Channel found ->  Adding message
                        ((JSONObject) arr.get(i)).getJSONArray("messages").put(message.toJson());
                    }
                }
                fileWriter.write(arr.toString(4)); // Pretty print with indentation
                fileWriter.flush();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject retrieveChannel(int channelID) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return new JSONObject(); // Return empty object if file doesn't exist
        }

        try {
            String content = Files.readString(file.toPath());
            JSONArray arr = new JSONArray(content);

            for (int i = 0; i<arr.length();i++){
                JSONObject j = (JSONObject) arr.get(i);
                if (j.getInt("id") == channelID) {
                    return j;
                }
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }

        return null;
    }
}
