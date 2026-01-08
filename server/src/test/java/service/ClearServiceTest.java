package service;

import dataaccess.*;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        clearService = new ClearService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Clear - Positive")
    public void clearPositive() throws DataAccessException {
        User user = new User("testuser", "password", "test@email.com");
        userDAO.createUser(user);
        authDAO.createAuth("testuser");
        gameDAO.createGame("TestGame");

        clearService.clear();

        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("testuser");
        });
    }
}