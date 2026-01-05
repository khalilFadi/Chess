package dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {
    private final Map<String, String> authTokens = new HashMap<>(); // token -> username

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        authTokens.put(authToken, username);
        return authToken;
    }

    public String getUsername(String authToken) throws DataAccessException {
        String username = authTokens.get(authToken);
        if (username == null) {
            throw new DataAccessException("Invalid auth token");
        }
        return username;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if (!authTokens.containsKey(authToken)) {
            throw new DataAccessException("Invalid auth token");
        }
        authTokens.remove(authToken);
    }

    public void clear() {
        authTokens.clear();
    }
}