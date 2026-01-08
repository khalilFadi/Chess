package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.RegisterRequest;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;
import model.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private UserService userService;
    private String authToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        RegisterRequest regRequest = new RegisterRequest("testuser", "password", "test@email.com");
        LoginResponse response = userService.register(regRequest);
        authToken = response.getAuthToken();
    }

    @Test
    @DisplayName("Create Game - Positive")
    public void createGamePositive() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest();
        request.setGameName("TestGame");
        CreateGameResponse response = gameService.createGame(request, authToken);

        assertNotNull(response);
        assertTrue(response.getGameID() > 0);
    }

    @Test
    @DisplayName("Create Game - Negative")
    public void createGameNegative() {
        CreateGameRequest request = new CreateGameRequest();
        request.setGameName(null);
        assertThrows(DataAccessException.class, () -> {
            gameService.createGame(request, authToken);
        });
    }

    @Test
    @DisplayName("List Games - Positive")
    public void listGamesPositive() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest();
        request.setGameName("TestGame");
        gameService.createGame(request, authToken);

        ListGamesResponse response = gameService.listGames(authToken);

        assertNotNull(response);
        assertNotNull(response.getGames());
        assertEquals(1, response.getGames().size());
    }

    @Test
    @DisplayName("List Games - Negative")
    public void listGamesNegative() {
        assertThrows(DataAccessException.class, () -> {
            gameService.listGames("invalid_token");
        });
    }

    @Test
    @DisplayName("Join Game - Positive")
    public void joinGamePositive() throws DataAccessException {
        CreateGameRequest createRequest = new CreateGameRequest();
        createRequest.setGameName("TestGame");
        CreateGameResponse createResponse = gameService.createGame(createRequest, authToken);

        JoinGameRequest joinRequest = new JoinGameRequest();
        joinRequest.setGameID(createResponse.getGameID());
        joinRequest.setPlayerColor("WHITE");

        gameService.joinGame(joinRequest, authToken);
        // Should not throw exception
    }

    @Test
    @DisplayName("Join Game - Negative")
    public void joinGameNegative() throws DataAccessException {
        CreateGameRequest createRequest = new CreateGameRequest();
        createRequest.setGameName("TestGame");
        CreateGameResponse createResponse = gameService.createGame(createRequest, authToken);

        JoinGameRequest joinRequest = new JoinGameRequest();
        joinRequest.setGameID(createResponse.getGameID());
        joinRequest.setPlayerColor(null);

        assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(joinRequest, authToken);
        });
    }
}