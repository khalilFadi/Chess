package dataaccess;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private UserDAO userDAO;
    @BeforeEach
    public void setUp() throws  DataAccessException{
        userDAO = new UserDAO();
        userDAO.clear();
    }

    @Test
    @DisplayName("Create User - Positive")
    public void createUserPositive() throws DataAccessException{
        User user = new User("testuser", "password123", "test@example.com");
        userDAO.createUser(user);

        // Verify user was created by retrieving it
        User retrievedUser = userDAO.getUser("testuser");
        assertNotNull(retrievedUser);
        assertEquals("testuser", retrievedUser.getUsername());
        assertEquals("test@example.com", retrievedUser.getEmail());
        assertNotNull(retrievedUser.getPassword());
    }

    @Test
    @DisplayName("Create User - Negative (Duplicate)")
    public void createUserNegative() throws DataAccessException{
        User user = new User("testuser", "password123", "test@example.com");
        userDAO.createUser(user);

        // Try to create the same user again - should throw exception
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user);
        });
    }

    @Test
    @DisplayName("Get User - Positive")
    public void getUserPositive() throws DataAccessException{
        User user = new User("testuser", "password123", "test@example.com");
        userDAO.createUser(user);
        User retrivedUser = userDAO.getUser("testuser");
        assertNotNull(retrivedUser);
        assertEquals("testuser", retrivedUser.getUsername());
        assertEquals("test@example.com", retrivedUser.getEmail());
    }

    @Test
    @DisplayName("Get User - Negative (Not Found)")
    public void getUserNegative(){
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("nonexistent");
        });
    }

    @Test
    @DisplayName("CLear Users - Positive")
    public void clearUserPositive() throws DataAccessException{
        User user = new User("testuser", "password123", "test@example.com");
        userDAO.createUser(user);

        assertNotNull(userDAO.getUser("testuser"));
        userDAO.clear();

        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("testuser");
        });
    }

}