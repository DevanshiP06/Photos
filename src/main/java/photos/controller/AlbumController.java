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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AlbumController {
    @FXML
    private Label albumTitleLabel;
    @FXML
    private ListView<HBox> photoListView;
    @FXML
    private TextField captionField;
    @FXML
    private TextField tagTypeField;
    @FXML
    private TextField tagValueField;
    @FXML
    private ListView<String> tagsList;
    @FXML
    private Label dateLabel;
    @FXML
    private Button addPhotoButton;

    private User user;
    private Album album;
    private int currentIndex = -1;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setUserAlbum(User u, Album a) {
        this.user = u;
        this.album = a;
        populateAlbum();
        setupPhotoSelection();
        albumTitleLabel.setText(a.getName());
        photoListView.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        photoListView.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    String lower = file.getName().toLowerCase();
                    if (lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                            || lower.endsWith(".bmp") || lower.endsWith(".gif")) {

                        boolean duplicate = false;
                        for (Photo p : album.getPhotos()) {
                            if (p.getFilePath().equals(file.getAbsolutePath())) {
                                duplicate = true;
                                break;
                            }
                        }
                        if (!duplicate) {
                            Photo newPhoto = new Photo(file.getAbsolutePath(), file.getName());
                            album.addPhoto(newPhoto);
                        }
                    }
                }
                UserManager.getInstance().saveUser(user);
                populateAlbum();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void populateAlbum() {
        photoListView.getItems().clear();

        for (Photo p : album.getPhotos()) {
            ImageView thumb = new ImageView(p.getThumbnail(100, 100));
            thumb.setSmooth(true);

            Label caption = new Label(p.getCaption());
            Label tagsLabel = new Label(p.getTags().stream()
                    .map(t -> t.getType() + "=" + t.getValue())
                    .collect(Collectors.joining(", ")));

            VBox vbox = new VBox(5);
            vbox.getChildren().add(caption);
            if (!p.getTags().isEmpty())
                vbox.getChildren().add(tagsLabel);

            HBox hbox = new HBox(10);
            hbox.getChildren().addAll(thumb, vbox);

            photoListView.getItems().add(hbox);
        }

        if (!album.getPhotos().isEmpty()) {
            currentIndex = 0;
            photoListView.getSelectionModel().select(currentIndex);
            showPhotoDetails();
        } else {
            currentIndex = -1;
            showPhotoDetails();
        }
    }

    private void setupPhotoSelection() {
        photoListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
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
        dateLabel.setText(p.getDate() != null ? dtFormatter.format(p.getDate()) : "Unknown");

        ObservableList<String> tagStrings = FXCollections.observableArrayList();
        for (Tag t : p.getTags()) {
            tagStrings.add(t.getType() + " = " + t.getValue());
        }
        tagsList.setItems(tagStrings);
    }

    @FXML
    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png"));

        java.util.List<File> files = fileChooser.showOpenMultipleDialog(addPhotoButton.getScene().getWindow());
        if (files == null || files.isEmpty())
            return;

        for (File file : files) {
            boolean exists = false;
            for (Photo p : album.getPhotos()) {
                if (p.getFilePath().equals(file.getAbsolutePath())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                new Alert(Alert.AlertType.ERROR, "Photo already exists: " + file.getName()).showAndWait();
                continue;
            }

            Photo newPhoto = new Photo(file.getAbsolutePath(), file.getName());
            album.addPhoto(newPhoto);
        }

        UserManager.getInstance().saveUser(user);
        populateAlbum();
    }

    @FXML
    private void onDeletePhoto() {
        if (currentIndex < 0)
            return;
        Photo toRemove = album.getPhotos().get(currentIndex);
        album.removePhoto(toRemove);
        UserManager.getInstance().saveUser(user);
        populateAlbum();
    }

    @FXML
    private void onUpdateCaption() {
        if (currentIndex < 0)
            return;
        String newCaption = captionField.getText().trim();
        if (newCaption.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter a caption").showAndWait();
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        p.setCaption(newCaption);
        UserManager.getInstance().saveUser(user);
        populateAlbum();
        photoListView.getSelectionModel().select(currentIndex);
    }

    @FXML
    private void onAddTag() {
        if (currentIndex < 0)
            return;
        String type = tagTypeField.getText().trim();
        String value = tagValueField.getText().trim();
        if (type.isEmpty() || value.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter tag type and value").showAndWait();
            return;
        }

        Photo p = album.getPhotos().get(currentIndex);
        Tag tag = new Tag(type, value);
        if (!p.getTags().contains(tag))
            p.addTag(tag);
        else
            new Alert(Alert.AlertType.ERROR, "Tag already exists").showAndWait();

        UserManager.getInstance().saveUser(user);
        tagTypeField.clear();
        tagValueField.clear();
        showPhotoDetails();
    }

    @FXML
    private void onDeleteTag() {
        if (currentIndex < 0)
            return;
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
        if (album.getPhotos().isEmpty())
            return;
        currentIndex = (currentIndex - 1 + album.getPhotos().size()) % album.getPhotos().size();
        photoListView.getSelectionModel().select(currentIndex);
        showPhotoDetails();
    }

    @FXML
    private void onNextPhoto() {
        if (album.getPhotos().isEmpty())
            return;
        currentIndex = (currentIndex + 1) % album.getPhotos().size();
        photoListView.getSelectionModel().select(currentIndex);
        showPhotoDetails();
    }

    @FXML
    private void onBack() {
        captionField.getScene().getWindow().hide();
    }
}
