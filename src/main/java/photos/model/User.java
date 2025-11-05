package photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password; // optional
    private List<Album> albums;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.albums = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public List<Album> getAlbums() { return albums; }

    public void addAlbum(Album album) { albums.add(album); }
    public void removeAlbum(Album album) { albums.remove(album); }
}
