package photos;

import photos.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final String FILE_PATH = "data/users/users.dat";

    // Load all users
    public static List<User> loadUsers() {
        File f = new File(FILE_PATH);
        if(!f.exists()) return new ArrayList<>();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<User>) ois.readObject();
        } catch(Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save a new user or update existing
    public static void saveUser(User user) {
        List<User> users = loadUsers();

        // replace if exists
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                saveAll(users);
                return;
            }
        }

        // else add new
        users.add(user);
        saveAll(users);
    }

    // Delete user
    public static void deleteUser(String username) {
        List<User> users = loadUsers();
        users.removeIf(u -> u.getUsername().equals(username));
        saveAll(users);
    }

    // Save full list
    private static void saveAll(List<User> users) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
