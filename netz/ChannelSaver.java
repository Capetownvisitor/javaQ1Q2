package netz;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ChannelSaver {

    public static ChannelSaver saver = null;

    public ChannelSaver() {
        if (saver == null) {
            saver = this;
        }
    }

    public static void saveChannels(ListView<Channel> channels) {
        String filePath = "netz/storage/channels.json";
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
        String filePath = "netz/storage/channels.json";
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

}
