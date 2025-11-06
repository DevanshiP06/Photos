package photos.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private List<User> users;
    private final String FILE_PATH = "data/users/users.dat";

    private UserManager() {
        users = loadUsers();
        ensureStockUserExists();
    }

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    public void createUser(String username, String password) {
        User u = new User(username, password);
        users.add(u);
        saveAll();
    }

    public void saveUser(User user) {
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                found = true;
                break;
            }
        }
        if (!found) {
            users.add(user);
        }

        saveAll();
    }

    public void deleteUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        saveAll();
    }

    private void saveAll() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // create directories if they don't exist
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        File f = new File(FILE_PATH);
        if (!f.exists())
            return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void ensureStockUserExists() {
    // Check if stock user already exists
    User stockUser = getUser("stock");
    if (stockUser != null) return; // already exists, nothing to do

    // Create stock user with password "stock"
    stockUser = new User("stock", "stock");

    // Create stock album
    Album stockAlbum = new Album("stock");

    // Load all photos from data/stock folder
    File stockFolder = new File("data/stock");
    if (stockFolder.exists() && stockFolder.isDirectory()) {
        File[] files = stockFolder.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") 
                    || lower.endsWith(".bmp") || lower.endsWith(".gif");
        });

        if (files != null) {
            for (File f : files) {
                Photo photo = new Photo(f.getPath(), f.getName());
                stockAlbum.addPhoto(photo);
            }
        }
    }

    stockUser.addAlbum(stockAlbum);
    users.add(stockUser);
    saveAll(); // save immediately
}

}
