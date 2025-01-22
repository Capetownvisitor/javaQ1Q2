package netz.ClientGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ChatApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./resources/fxml/login.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
