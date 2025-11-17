package photos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.Photo;
import photos.model.Tag;
import photos.model.User;
import photos.model.UserManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Search Photos");
        dialog.setHeaderText("Search by tag (format: type=value) or leave empty for all photos:");
        dialog.setContentText("Enter search query:");

        dialog.showAndWait().ifPresent(query -> {
            List<Photo> results = new ArrayList<>();

            String[] parts = query.split("=");

            if (parts.length == 2) {
                String type = parts[0].trim();
                String value = parts[1].trim();

                for (Album a : user.getAlbums()) {
                    for (Photo p : a.getPhotos()) {
                        for (Tag t : p.getTags()) {
                            if (t.getType().equalsIgnoreCase(type) && t.getValue().equalsIgnoreCase(value)) {
                                if (!results.contains(p))
                                    results.add(p);
                            }
                        }
                    }
                }
            } else if (query.isBlank()) {
                for (Album a : user.getAlbums()) {
                    for (Photo p : a.getPhotos()) {
                        if (!results.contains(p))
                            results.add(p);
                    }
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid query format. Use type=value").showAndWait();
                return;
            }

            if (results.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No photos found").showAndWait();
                return;
            }

            Album searchAlbum = new Album("Search Results");
            for (Photo p : results)
                searchAlbum.addPhoto(p);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/AlbumView.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Search Results");

                AlbumController controller = loader.getController();
                controller.setUserAlbum(user, searchAlbum);

                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
