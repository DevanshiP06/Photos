package photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String name;
    private List<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Photo> getPhotos() { return photos; }
    public void addPhoto(Photo p) { photos.add(p); }
    public void removePhoto(Photo p) { photos.remove(p); }
    @Override
    public String toString() { return this.getName(); }

}
