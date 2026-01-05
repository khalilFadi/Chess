    package dataaccess;
import chess.ChessGame;
import kotlin.DslMarker;
import model.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GameDAOTest{
    private GameDAO gameDAO;
    @BeforeEach
    public void setUp() throws DataAccessException{
        gameDAO = new GameDAO();
        gameDAO.clear();
    }

    @Test
    @DisplayName("Create Game - Positive")
    public void createGamePostive() throws DataAccessException{
        String gameName = "Test Game";
        int gameID = gameDAO.createGame(gameName);

        assertTrue(gameID > 0, "Game ID should be positive");
        Game game = gameDAO.getGame(gameID);
        assertNotNull(game);
        assertEquals(gameName, game.getGameName());
        assertNotNull(game.getGame());
        assertNotNull(game.getGame().getBoard());
    }

    @Test
    @DisplayName("Get Game - Positive")
    public void getGamePositive() throws DataAccessException{
        String gameName = "Test Game";
        int gameID = gameDAO.createGame(gameName);
        Game game = gameDAO.getGame(gameID);
        assertNotNull(game);
        assertEquals(gameID, game.getGameID());
        assertEquals(gameName, game.getGameName());
        assertNotNull(game.getGame());
    }

    @Test
    @DisplayName("Get Game - Negative (Not Foumd)")
    public void getGameNegative(){
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(999999);
        });
    }

    @Test
    @DisplayName("Get All Games - Positive")
    public void getAllGamesPositive() throws DataAccessException {
        int gameID1 = gameDAO.createGame("Game 1 ");
        int gameID2 = gameDAO.createGame("Game 2 ");
        int gameID3 = gameDAO.createGame("Game 3 ");

        var allGames = gameDAO.getAllGames();
        assertNotNull(allGames);
        assertEquals(3, allGames.size());
        assertTrue(allGames.containsKey(gameID1));
        assertTrue(allGames.containsKey(gameID2));
        assertTrue(allGames.containsKey(gameID3));
    }

    @Test
    @DisplayName("Update Game - Negative (Not Found)")
    public void updateGameNegative() throws DataAccessException {
        Game game = new Game();
        game.setGameID(99999);
        game.setGameName("Fake Game");
        game.setGame(new ChessGame());

        assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(game);
        });
    }

    @Test
    @DisplayName("Clear Games - Positive")
    public void clearGamesPositive() throws DataAccessException{
        int gameID1 = gameDAO.createGame("Game 1");
        int gameID2 = gameDAO.createGame("Game 2");

        assertNotNull(gameDAO.getGame(gameID1));
        assertNotNull(gameDAO.getGame(gameID2));

        gameDAO.clear();
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(gameID1);
        });
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(gameID2);
        });
    }

    @Test
    @DisplayName("Update Game - Positive")
    public void updateGamePositive() throws DataAccessException{
        String gameName = "Test Game";
        int gameID = gameDAO.createGame(gameName);

        Game game = gameDAO.getGame(gameID);
        game.setWhiteUserName("player1");
        game.setBlackUserName("player2");

        gameDAO.updateGame(game);

        Game updatedgame = gameDAO.getGame(gameID);
        assertEquals("player1", updatedgame.getWhiteUserName());
        assertEquals("player2", updatedgame.getBlackUserName());
    }
}
