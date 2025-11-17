package photos;

import photos.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final String FILE_PATH = "data/users/users.dat";

    public static List<User> loadUsers() {
        File f = new File(FILE_PATH);
        List<User> users;

        if (!f.exists()) {
            users = new ArrayList<>();
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                users = (List<User>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                users = new ArrayList<>();
            }
        }

        return users;
    }

    public static void saveUser(User user) {
        List<User> users = loadUsers();

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (!found) users.add(user);

        saveAll(users);
    }

    public static void deleteUser(String username) {
        List<User> users = loadUsers();
        users.removeIf(u -> u.getUsername().equals(username));
        saveAll(users);
    }

    private static void saveAll(List<User> users) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
