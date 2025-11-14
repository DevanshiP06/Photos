package photos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import photos.StorageManager;
import photos.model.User;

import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> usersList;
    @FXML
    private TextField newUserField;
    @FXML
    private PasswordField newPassField;

    private ObservableList<String> userNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        refreshUsers();
    }

    private void refreshUsers() {
        List<User> users = photos.model.UserManager.getInstance().getUsers();
        userNames.clear();
        for (User u : users) {
            if (!u.getUsername().equals("admin"))
                userNames.add(u.getUsername());
        }
        usersList.setItems(userNames);
    }

    @FXML
    private void onCreateUser() {
        String username = newUserField.getText().trim();
        String password = newPassField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter username and password.").showAndWait();
            return;
        }

        User newUser = new User(username, password);
        photos.model.UserManager.getInstance().saveUser(newUser);
        refreshUsers();
        newUserField.clear();
        newPassField.clear();
    }

    @FXML
    private void onDeleteUser() {
        String selected = usersList.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        if (selected.equals("stock")) {
            new Alert(Alert.AlertType.ERROR, "Stock user cannot be deleted!").showAndWait();
            return;
        }

        photos.model.UserManager.getInstance().deleteUser(selected);
        refreshUsers();
    }

    @FXML
    private void onLogout() {
        try {
            Stage stage = (Stage) usersList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/Login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Photos App - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
