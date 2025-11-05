package photos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent e) {
        String user = usernameField.getText().trim();
        if (user.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Please enter a username.");
            a.showAndWait();
            return;
        }
        // Temporary: show a simple info dialog. We'll wire actual login later.
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Logged in as: " + user + "\n(Placeholder)");
        a.showAndWait();
    }

    @FXML
    private void onQuit(ActionEvent e) {
        // close the app
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.close();
    }
}