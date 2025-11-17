package photos.model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User implements Serializable {
    private String username;
    private String passwordHash;
    private String salt;
    private List<Album> albums;

    public User(String username, String plainPassword) {
        this.username = username;
        this.albums = new ArrayList<>();
        generatePasswordHash(plainPassword);
    }

    public String getUsername() { return username; }

    public List<Album> getAlbums() { return albums; }

    public void addAlbum(Album album) { albums.add(album); }
    public void removeAlbum(Album album) { albums.remove(album); }

    private void generatePasswordHash(String plainPassword) {
        try {
            SecureRandom sr = new SecureRandom();
            byte[] saltBytes = new byte[16];
            sr.nextBytes(saltBytes);
            this.salt = Base64.getEncoder().encodeToString(saltBytes);
            this.passwordHash = hashPassword(plainPassword, saltBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password, byte[] saltBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean verifyPassword(String password) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(this.salt);
            String attemptedHash = hashPassword(password, saltBytes);
            return attemptedHash.equals(this.passwordHash);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPassword(String newPassword) {
        generatePasswordHash(newPassword);
    }
}
