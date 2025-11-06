package photos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import photos.model.Album;
import photos.model.User;
import photos.model.UserManager;

public class UserController {

    @FXML private ListView<String> albumsList;
    @FXML private TextField newAlbumField;

    private User user;

    public void setUser(User u) {
        this.user = u;
        refreshAlbums();
    }

    private void refreshAlbums() {
        albumsList.getItems().clear();
        for(Album a : user.getAlbums()) {
            albumsList.getItems().add(a.getName());
        }
    }

    @FXML
    private void onAddAlbum() {
        String name = newAlbumField.getText().trim();
        if(name.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter album name").showAndWait();
            return;
        }
        for(Album a : user.getAlbums()) {
            if(a.getName().equals(name)) {
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
    private void onDeleteAlbum() {
        String selected = albumsList.getSelectionModel().getSelectedItem();
        if(selected == null) return;
        Album toRemove = null;
        for(Album a : user.getAlbums()) {
            if(a.getName().equals(selected)) { toRemove = a; break; }
        }
        if(toRemove != null) {
            user.removeAlbum(toRemove);
            UserManager.getInstance().saveUser(user);
            refreshAlbums();
        }
    }
}
