package photos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import photos.StorageManager;
import photos.model.User;

import java.util.List;

public class AdminController {

    @FXML private ListView<String> usersList;
    @FXML private TextField newUserField;
    @FXML private PasswordField newPassField;

    private ObservableList<String> userNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        refreshUsers();
    }

    private void refreshUsers() {
        List<User> users = StorageManager.loadUsers();
        userNames.clear();
        for(User u : users) {
            if(!u.getUsername().equals("admin"))
                userNames.add(u.getUsername());
        }
        usersList.setItems(userNames);
    }

    @FXML
    private void onCreateUser() {
        String username = newUserField.getText().trim();
        String password = newPassField.getText().trim();

        if(username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter username and password.").showAndWait();
            return;
        }

        User newUser = new User(username, password);
        StorageManager.saveUser(newUser);
        refreshUsers();
        newUserField.clear();
        newPassField.clear();
    }

    @FXML
    private void onDeleteUser() {
        String selected = usersList.getSelectionModel().getSelectedItem();
        if(selected == null) return;

        StorageManager.deleteUser(selected);
        refreshUsers();
    }
}
