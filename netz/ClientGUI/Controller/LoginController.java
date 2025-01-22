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

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    ChatApplication mainApp;

    public void setMainApp(ChatApplication mainApp){
        this.mainApp = mainApp;
    }

    public void onLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            mainApp.logIn(username, password);
        }catch (Exception e){
            // TODO: add Error
            e.printStackTrace();
        }
    }
}
