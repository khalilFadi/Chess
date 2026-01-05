package dataaccess;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private final Map<String, User> users = new HashMap<>();

    public void createUser(User user) throws DataAccessException {
        if (users.containsKey(user.getUsername())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) throws DataAccessException {
        User user = users.get(username);
        if (user == null) {
            throw new DataAccessException("User not found");
        }
        return user;
    }

    public void clear() {
        users.clear();
    }
}