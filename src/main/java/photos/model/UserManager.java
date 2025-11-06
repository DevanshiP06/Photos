package photos.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private List<User> users;
    private final String FILE_PATH = "users.dat";

    private UserManager() {
        users = loadUsers();
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public void createUser(String username, String password) {
        User u = new User(username, password);
        users.add(u);
        saveAll();
    }

    public void saveUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                saveAll();
                return;
            }
        }
    }

    public void deleteUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        saveAll();
    }

    private void saveAll() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
