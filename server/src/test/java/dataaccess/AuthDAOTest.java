package dataaccess;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException{
        authDAO = new AuthDAO();
        authDAO.clear();
    }

    @Test
    @DisplayName("Create Auth - Positive")
    public void createAuthPositive() throws DataAccessException{
        String username = "testuser";
        String authToken = authDAO.createAuth(username);

        assertNotNull(authToken);
        assertFalse(authToken.isEmpty());

        String retrivedUsername = authDAO.getUsername(authToken);
        assertEquals(username, retrivedUsername);
    }

    @Test
    @DisplayName("Get Username - Positive")
    public void getUsernamePositive() throws DataAccessException{
        String username = "testuser";
        String authToken = authDAO.createAuth(username);

        String retrievedUsername = authDAO.getUsername(authToken);  // Use getUsername, not createAuth
        assertEquals(username, retrievedUsername);
    }

    @Test
    @DisplayName("Delete Auth - Positive")
    public void deleteAuthPositive() throws DataAccessException{
        String username = "testuser";
        String authToken = authDAO.createAuth(username);

        assertNotNull(authDAO.getUsername(authToken));

        authDAO.deleteAuth(authToken);
        assertThrows(DataAccessException.class, () -> {
            authDAO.getUsername(authToken);
        });
    }

    @Test
    @DisplayName("Delete Auth - Negative(Invlaid Token)")
    public void deleteAuthNegative(){
        assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth("invalid-token");
        });
    }

    @Test
    @DisplayName("Clear Auth - Positive")
    public void clearAuthPositive() throws DataAccessException {
        String token1 = authDAO.createAuth("user1");
        String token2 = authDAO.createAuth("user2");

        assertNotNull(authDAO.getUsername(token1));
        assertNotNull(authDAO.getUsername(token2));

        authDAO.clear();
        assertThrows(DataAccessException.class, () -> {
            authDAO.getUsername(token1);
        });
        assertThrows(DataAccessException.class, () -> {
            authDAO.getUsername(token2);
        });
    }
}
