package client;

import chess.ChessGame;
import client.model.response.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }
    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    //actual tests
    @Test
    public void registerPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@game");
        assertNotNull(authData);
        assertNotNull(authData.getAuthToken());
        assertTrue(authData.getAuthToken().length() > 10);
        assertEquals("player1", authData.getUsername());
    }

    @Test
    public void registerNegative() {
        assertThrows(Exception.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            facade.register("player1", "password", "p1@email.com"); // Duplicate username
        });
    }

    @Test
    public void loginPositive() throws Exception {
        facade.register("player2", "password", "p2@email.com");
        var authData = facade.login("player2", "password");

        assertNotNull(authData);
        assertNotNull(authData.getAuthToken());
        assertTrue(authData.getAuthToken().length() > 10);
        assertEquals("player2", authData.getUsername());
    }
    @Test
    public void logoutPositive() throws Exception {
        var authData = facade.register("player3", "password", "p3@email.com");
        facade.logout(authData.getAuthToken()); // Should not throw
        assertTrue(true); // If we get here, logout succeeded
    }

    @Test
    public void logoutNegative() {
        assertThrows(Exception.class, () -> {
            facade.logout("invalid_token");
        });
    }
    @Test
    public void loginNegative() {
        assertThrows(Exception.class, () -> {
            facade.login("nonexistent", "wrong");
        });
    }
    @Test
    public void createGamePositive() throws Exception {
        var authData = facade.register("player4", "password", "p4@email.com");
        var response = facade.createGame(authData.getAuthToken(), "TestGame");

        assertNotNull(response);
        assertNotNull(response.getGameID());
        assertTrue(response.getGameID() > 0);
    }

    @Test
    public void createGameNegative() {
        assertThrows(Exception.class, () -> {
            facade.createGame("invalid_token", "TestGame");
        });
    }

    @Test
    public void listGamesPositive() throws Exception {
        var authData = facade.register("player5", "password", "p5@email.com");
        facade.createGame(authData.getAuthToken(), "Game1");
        facade.createGame(authData.getAuthToken(), "Game2");

        var response = facade.listGames(authData.getAuthToken());

        assertNotNull(response);
        assertNotNull(response.getGames());
        assertTrue(response.getGames().size() >= 2);

        // Check that our games are in the list
        var gameNames = response.getGames().stream()
                .map(g -> g.getGameName())
                .toList();
        assertTrue(gameNames.contains("Game1"));
        assertTrue(gameNames.contains("Game2"));
    }

    @Test
    public void listGamesNegative() {
        assertThrows(Exception.class, () -> {
            facade.listGames("invalid_token");
        });
    }
    @Test
    public void joinGamePositive() throws Exception {
        var authData = facade.register("player6", "password", "p6@email.com");
        var gameResponse = facade.createGame(authData.getAuthToken(), "JoinTest");

        facade.joinGame(authData.getAuthToken(), gameResponse.getGameID(), ChessGame.TeamColor.WHITE);
        // Should not throw - if we get here, join succeeded
        assertTrue(true);
    }

    @Test
    public void joinGameNegative() {
        assertThrows(Exception.class, () -> {
            facade.joinGame("invalid_token", 999, ChessGame.TeamColor.WHITE);
        });
    }

}
