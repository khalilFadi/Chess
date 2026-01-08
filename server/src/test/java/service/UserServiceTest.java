package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Register - Positive")
    public void registerPositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testuser", "password", "test@email.com");
        LoginResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertNotNull(response.getAuthToken());
    }

    @Test
    @DisplayName("Register - Negative")
    public void registerNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testuser", "password", "test@email.com");
        userService.register(request);

        assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    @DisplayName("Login - Positive")
    public void loginPositive() throws DataAccessException {
        RegisterRequest regRequest = new RegisterRequest("testuser", "password", "test@email.com");
        userService.register(regRequest);

        LoginRequest request = new LoginRequest("testuser", "password");
        LoginResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertNotNull(response.getAuthToken());
    }

    @Test
    @DisplayName("Login - Negative")
    public void loginNegative() throws DataAccessException {
        RegisterRequest regRequest = new RegisterRequest("testuser", "password", "test@email.com");
        userService.register(regRequest);

        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        assertThrows(DataAccessException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    @DisplayName("Logout - Positive")
    public void logoutPositive() throws DataAccessException {
        RegisterRequest regRequest = new RegisterRequest("testuser", "password", "test@email.com");
        LoginResponse regResponse = userService.register(regRequest);

        userService.logout(regResponse.getAuthToken());
        // Should not throw exception
    }

    @Test
    @DisplayName("Logout - Negative")
    public void logoutNegative() {
        assertThrows(DataAccessException.class, () -> {
            userService.logout("invalid_token");
        });
    }
}