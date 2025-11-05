package photos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class UserController {

    @FXML
    private void initialize() {
        // later initialization code loading albums
    }

    @FXML
    private void addAlbum() {
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Add album clicked (placeholder)");
        a.showAndWait();
    }
}
