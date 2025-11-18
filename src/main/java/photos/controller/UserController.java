package photos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.User;
import photos.model.UserManager;
import java.io.IOException;


public class UserController {

    @FXML
    private ListView<String> albumsList;
    @FXML
    private TextField newAlbumField;

    private User user;

    public void setUser(User u) {
        this.user = u;
        refreshAlbums();
        setupAlbumDoubleClick();
    }

    private void refreshAlbums() {
        albumsList.getItems().clear();
        for (Album a : user.getAlbums()) {
            albumsList.getItems().add(a.getName());
        }
    }

    @FXML
    private void onAddAlbum() {
        String name = newAlbumField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter album name").showAndWait();
            return;
        }
        for (Album a : user.getAlbums()) {
            if (a.getName().equals(name)) {
                new Alert(Alert.AlertType.ERROR, "Album already exists!").showAndWait();
                return;
            }
        }
        Album album = new Album(name);
        user.addAlbum(album);
        UserManager.getInstance().saveUser(user);
        newAlbumField.clear();
        refreshAlbums();
    }

    @FXML
    private void onRenameAlbum() {
        String selected = albumsList.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        TextInputDialog dialog = new TextInputDialog(selected);
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Enter new album name:");
        dialog.showAndWait().ifPresent(newName -> {
            if (newName.trim().isEmpty())
                return;
            for (Album a : user.getAlbums()) {
                if (a.getName().equals(newName)) {
                    new Alert(Alert.AlertType.ERROR, "Album with this name already exists!").showAndWait();
                    return;
                }
            }
            Album a = user.getAlbum(selected);
            a.setName(newName.trim());
            UserManager.getInstance().saveUser(user);
            refreshAlbums();
        });
    }

    @FXML
    private void onDeleteAlbum() {
        String selected = albumsList.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        Album toRemove = null;
        for (Album a : user.getAlbums()) {
            if (a.getName().equals(selected)) {
                toRemove = a;
                break;
            }
        }
        if (toRemove != null) {
            user.removeAlbum(toRemove);
            UserManager.getInstance().saveUser(user);
            refreshAlbums();
        }
    }

    private void setupAlbumDoubleClick() {
        albumsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = albumsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openAlbum(selected);
                }
            }
        });
    }

    private void openAlbum(String albumName) {
        Album album = null;
        for (Album a : user.getAlbums()) {
            if (a.getName().equals(albumName)) {
                album = a;
                break;
            }
        }
        if (album == null)
            return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/AlbumView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Album - " + albumName);

            AlbumController controller = loader.getController();
            controller.setUserAlbum(user, album);

            stage.showAndWait();

            UserManager.getInstance().saveUser(user);
            refreshAlbums();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchPhotos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/SearchView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Search Photos");

            SearchController controller = loader.getController();
            controller.setUser(user);

            stage.showAndWait();

            UserManager.getInstance().saveUser(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout() {
        try {
            Stage stage = (Stage) albumsList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/Login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Photos App - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
