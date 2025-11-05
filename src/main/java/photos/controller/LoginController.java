package photos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent e) {
        String user = usernameField.getText().trim();
        if (user.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a username.").showAndWait();
            return;
        }

        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            if (user.equals("admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
                stage.setScene(new Scene(loader.load()));
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User.fxml"));
                stage.setScene(new Scene(loader.load()));
            }
            stage.setTitle("Photos - " + user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onQuit(ActionEvent e) {
        // close the app
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.close();
    }
}