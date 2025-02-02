package netz;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class UserStorage {

    private static final String filePath = "netz/storage/users.json";

    public static void saveUsers(ArrayList<User> users) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try ( FileWriter fileWriter = new FileWriter(file, false)) { // append -> false => overwrite
            JSONArray jsonArray = new JSONArray();
            for (User user : users) {
                jsonArray.put(user.toJson());
            }
            fileWriter.write(jsonArray.toString(4)); // Pretty print with indentation
            fileWriter.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JSONArray getUsers() {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return new JSONArray();
        }

        try ( FileReader fileReader = new FileReader(file)) {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            return new JSONArray(content);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }

    public static JSONObject getUserById(int userID){
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i<jsonArray.length();i++){
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                if (userObject.getInt("id") == userID){
                    return userObject;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getUserByConnection(String userIP, int userPort){
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i<jsonArray.length();i++){
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                if (userObject.getString("ipAddress").equals(userIP) && userObject.getInt("port") == userPort){
                    return userObject;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getUserByName(String userName){
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i<jsonArray.length();i++){
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                if (userObject.getString("nickname").equals(userName)){
                    return userObject;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
