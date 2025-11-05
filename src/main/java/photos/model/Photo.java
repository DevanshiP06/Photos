package photos.model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private String caption;
    private String filePath;
    private LocalDateTime date; // last modification date as proxy
    private List<Tag> tags;

    public Photo(String filePath, String caption) {
        this.filePath = filePath;
        this.caption = caption;
        this.date = LocalDateTime.ofEpochSecond(new File(filePath).lastModified() / 1000, 0, java.time.ZoneOffset.UTC);
        this.tags = new ArrayList<>();
    }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getFilePath() { return filePath; }
    public LocalDateTime getDate() { return date; }

    public List<Tag> getTags() { return tags; }

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
