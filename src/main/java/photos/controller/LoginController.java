package photos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photos.model.User;
import photos.model.UserManager;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent e) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter username and password.").showAndWait();
            return;
        }

        if (user.equals("admin") && pass.equals("admin")) {
            loadScreen("/photos/view/Admin.fxml", "Admin");
            return;
        }

        for (User u : UserManager.getInstance().getUsers()) {
            if (u.getUsername().equals(user) && u.verifyPassword(pass)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/User.fxml"));
                try {
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.setTitle("Photos - " + user);

                    photos.controller.UserController controller = loader.getController();
                    controller.setUser(u);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }

        new Alert(Alert.AlertType.ERROR, "Invalid username or password!").showAndWait();
    }

    private void loadScreen(String fxml, String title) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Photos - " + title);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onQuit(ActionEvent e) {
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.close();
    }
}
