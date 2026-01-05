package client;

import org.junit.jupiter.api.*;
import server.Server;


import chess.ChessGame;
import client.model.response.*;
import org.junit.jupiter.api.*;

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


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    //actual tests
    @Test
    public void registerPositive throws Exception {
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

    

}
