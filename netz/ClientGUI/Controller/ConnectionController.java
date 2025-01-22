package netz.ClientGUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import netz.ClientGUI.ChatApplication;

import java.io.IOException;


public class ConnectionController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField serverIPField;

    @FXML
    TextField serverPortField;

    ChatApplication mainApp;

    public void setMainApp(ChatApplication mainApp){
        this.mainApp = mainApp;
    }

    public void onConnect(ActionEvent event) throws IOException {

        String ip = serverIPField.getText();
        String port = serverPortField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/login.fxml"));
        root = loader.load();

        try {
            mainApp.connectToServer(ip, Integer.parseInt(port));
            //mainApp.connected = true;
        }catch (Exception e){
            // display an error in the GUI
            // TODO: Display the connection
            e.printStackTrace();
        }
        /*
        if (mainApp.connected){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }*/

    }
}
