package photos;

import photos.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final String USERS_DIR = "data/users";

    // Load all users from data/users/
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File dir = new File(USERS_DIR);
        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files != null) {
            for (File f : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                    User u = (User) ois.readObject();
                    users.add(u);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

    // Save a single user to file
    public static void saveUser(User user) {
        File dir = new File(USERS_DIR);
        if (!dir.exists()) dir.mkdirs();
        File f = new File(dir, user.getUsername() + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete user file
    public static void deleteUser(String username) {
        File f = new File(USERS_DIR, username + ".ser");
        if (f.exists()) f.delete();
    }
}
