package photos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import photos.model.Album;
import photos.model.Photo;
import photos.model.Tag;
import photos.model.User;
import photos.model.UserManager;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class AlbumController {

    @FXML private ListView<String> photosList;
    @FXML private TextField captionField;
    @FXML private TextField tagTypeField;
    @FXML private TextField tagValueField;
    @FXML private ListView<String> tagsList;
    @FXML private Label dateLabel;
    @FXML private Button addPhotoButton;

    private User user;
    private Album album;
    private ObservableList<String> photoNames = FXCollections.observableArrayList();
    private int currentIndex = -1; // for previous/next navigation

    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setUserAlbum(User u, Album a) {
        this.user = u;
        this.album = a;
        refreshPhotos();
        setupPhotoSelection();
    }

    private void refreshPhotos() {
        photoNames.clear();
        for (Photo p : album.getPhotos()) {
            photoNames.add(p.getCaption() + " (" + p.getFilePath() + ")");
        }
        photosList.setItems(photoNames);

        // Reset current index
        if (!album.getPhotos().isEmpty()) {
            currentIndex = 0;
            photosList.getSelectionModel().select(currentIndex);
            showPhotoDetails();
        } else {
            currentIndex = -1;
            showPhotoDetails();
        }
    }

    private void setupPhotoSelection() {
        photosList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            currentIndex = newVal.intValue();
            showPhotoDetails();
        });
    }

    private void showPhotoDetails() {
        if (currentIndex < 0 || currentIndex >= album.getPhotos().size()) {
            captionField.clear();
            tagsList.setItems(FXCollections.observableArrayList());
            dateLabel.setText("Unknown");
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        captionField.setText(p.getCaption());

        if (p.getDate() != null) {
            dateLabel.setText(dtFormatter.format(p.getDate()));
        } else {
            dateLabel.setText("Unknown");
        }

        ObservableList<String> tagStrings = FXCollections.observableArrayList();
        for (Tag t : p.getTags()) {
            tagStrings.add(t.getType() + " = " + t.getValue());
        }
        tagsList.setItems(tagStrings);
    }

    @FXML
    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png")
        );
        File file = fileChooser.showOpenDialog(addPhotoButton.getScene().getWindow());
        if (file == null) return;

        // Check duplicate
        for (Photo p : album.getPhotos()) {
            if (p.getFilePath().equals(file.getAbsolutePath())) {
                new Alert(Alert.AlertType.ERROR, "Photo already exists in this album!").showAndWait();
                return;
            }
        }

        Photo newPhoto = new Photo(file.getAbsolutePath(), file.getName());
        album.addPhoto(newPhoto);
        UserManager.getInstance().saveUser(user);
        refreshPhotos();
    }

    @FXML
    private void onDeletePhoto() {
        if (currentIndex < 0) return;

        Photo toRemove = album.getPhotos().get(currentIndex);
        album.removePhoto(toRemove);
        UserManager.getInstance().saveUser(user);
        refreshPhotos();
    }

    @FXML
    private void onUpdateCaption() {
        if (currentIndex < 0) return;
        String newCaption = captionField.getText().trim();
        if (newCaption.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter a caption").showAndWait();
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        p.setCaption(newCaption);
        UserManager.getInstance().saveUser(user);
        refreshPhotos();
    }

    @FXML
    private void onAddTag() {
        if (currentIndex < 0) return;
        String type = tagTypeField.getText().trim();
        String value = tagValueField.getText().trim();
        if (type.isEmpty() || value.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter tag type and value").showAndWait();
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        Tag tag = new Tag(type, value);
        if (!p.getTags().contains(tag)) {
            p.addTag(tag);
        } else {
            new Alert(Alert.AlertType.ERROR, "Tag already exists").showAndWait();
        }

        UserManager.getInstance().saveUser(user);
        tagTypeField.clear();
        tagValueField.clear();
        showPhotoDetails();
    }

    @FXML
    private void onDeleteTag() {
        if (currentIndex < 0) return;
        String type = tagTypeField.getText().trim();
        String value = tagValueField.getText().trim();
        if (type.isEmpty() || value.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter tag type and value").showAndWait();
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        Tag tag = new Tag(type, value);
        p.removeTag(tag);

        UserManager.getInstance().saveUser(user);
        tagTypeField.clear();
        tagValueField.clear();
        showPhotoDetails();
    }

    @FXML
    private void onPreviousPhoto() {
        if (album.getPhotos().isEmpty()) return;
        currentIndex = (currentIndex - 1 + album.getPhotos().size()) % album.getPhotos().size();
        photosList.getSelectionModel().select(currentIndex);
        showPhotoDetails();
    }

    @FXML
    private void onNextPhoto() {
        if (album.getPhotos().isEmpty()) return;
        currentIndex = (currentIndex + 1) % album.getPhotos().size();
        photosList.getSelectionModel().select(currentIndex);
        showPhotoDetails();
    }

    @FXML
    private void onBack() {
        // Close album window
        captionField.getScene().getWindow().hide();
    }
}
