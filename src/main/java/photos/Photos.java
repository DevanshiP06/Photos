package photos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Photos App - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // in your assignment, handle this with an Alert instead
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
