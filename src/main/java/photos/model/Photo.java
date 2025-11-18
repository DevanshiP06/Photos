package photos.model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String caption;
    private String filePath;
    private LocalDateTime date;
    private List<Tag> tags;

    public Photo(String filePath, String caption) {
        this.filePath = filePath;
        this.caption = caption;
        this.date = LocalDateTime.ofEpochSecond(new File(filePath).lastModified() / 1000, 0, java.time.ZoneOffset.UTC);
        this.tags = new ArrayList<>();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        if (!tags.contains(tag))
            tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public Image getThumbnail(double width, double height) {
        try {
            Image img = new Image(new FileInputStream(filePath), width, height, true, true);
            return img;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public javafx.scene.image.Image getFullImage() {
        return new javafx.scene.image.Image("file:" + filePath);
    }

}
