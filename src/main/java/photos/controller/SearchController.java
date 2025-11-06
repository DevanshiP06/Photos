package photos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.Photo;
import photos.model.Tag;
import photos.model.User;
import photos.model.UserManager;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TextField tag1Type, tag1Value, tag2Type, tag2Value;
    @FXML private ChoiceBox<String> tagOperator;
    @FXML private ListView<String> resultsList;

    private User user;
    private List<Photo> searchResults = new ArrayList<>();

    public void setUser(User u) {
        this.user = u;
    }

    @FXML
    private void onSearchByDate() {
        searchResults.clear();
        resultsList.getItems().clear();

        String startStr = startDateField.getText().trim();
        String endStr = endDateField.getText().trim();
        if (startStr.isEmpty() || endStr.isEmpty()) {
            showAlert("Enter both start and end dates.");
            return;
        }

        try {
            LocalDate start = LocalDate.parse(startStr, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endStr, DateTimeFormatter.ISO_LOCAL_DATE);

            for (Album a : user.getAlbums()) {
                for (Photo p : a.getPhotos()) {
                    LocalDate photoDate = p.getDate().toLocalDate();
                    if (!searchResults.contains(p) && !photoDate.isBefore(start) && !photoDate.isAfter(end)) {
                        searchResults.add(p);
                        resultsList.getItems().add(a.getName() + " - " + p.getCaption());
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Invalid date format. Use yyyy-mm-dd.");
        }
    }

    @FXML
    private void onSearchByTags() {
        searchResults.clear();
        resultsList.getItems().clear();

        String t1 = tag1Type.getText().trim();
        String v1 = tag1Value.getText().trim();
        if (t1.isEmpty() || v1.isEmpty()) {
            showAlert("Enter at least first tag type and value.");
            return;
        }

        String t2 = tag2Type.getText().trim();
        String v2 = tag2Value.getText().trim();
        String op = tagOperator.getValue();

        for (Album a : user.getAlbums()) {
            for (Photo p : a.getPhotos()) {
                boolean matches = false;

                boolean first = p.getTags().stream().anyMatch(tag -> tag.getType().equals(t1) && tag.getValue().equals(v1));

                if (!t2.isEmpty() && !v2.isEmpty()) {
                    boolean second = p.getTags().stream().anyMatch(tag -> tag.getType().equals(t2) && tag.getValue().equals(v2));
                    if ("AND".equals(op)) matches = first && second;
                    else matches = first || second;
                } else {
                    matches = first;
                }

                if (matches && !searchResults.contains(p)) {
                    searchResults.add(p);
                    resultsList.getItems().add(a.getName() + " - " + p.getCaption());
                }
            }
        }
    }

    @FXML
    private void onCreateAlbum() {
        if (searchResults.isEmpty()) {
            showAlert("No search results to create album from.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter new album name:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) return;

            for (Album a : user.getAlbums()) {
                if (a.getName().equals(name)) {
                    showAlert("Album with this name already exists.");
                    return;
                }
            }

            Album newAlbum = new Album(name);
            for (Photo p : searchResults) {
                newAlbum.addPhoto(p);
            }
            user.addAlbum(newAlbum);
            UserManager.getInstance().saveUser(user);
        });
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) resultsList.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
