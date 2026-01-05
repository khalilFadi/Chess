package dataaccess;

import chess.ChessGame;
import model.Game;
import com.google.gson.Gson;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;

public class GameDAO {
    private final Gson gson = new Gson();

    public GameDAO() throws DataAccessException {
        configureDatabase();
    }
    private void configureDatabase() throws DataAccessException{
        var createGameTable = """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                gameName VARCHAR(255) NOT NULL,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                game TEXT NOT NULL
            )""";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(createGameTable)){
                preparedStatement.executeUpdate();
            }
        }catch ( SQLException e){
            throw new DataAccessException("Unable to configure database", e);
        }
    }
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameName, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?)";
        ChessGame newChessGame = new ChessGame();
        newChessGame.getBoard().resetBoard();
        String gameJson = gson.toJson(newChessGame);
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, java.sql.Statement.RETURN_GENERATED_KEYS)){                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setString(4, gameJson);
                preparedStatement.executeUpdate();
                try (var rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }catch (SQLException e){
        throw new DataAccessException("Unable to create game", e);
    }
    throw new DataAccessException("Unable to create game - no ID generated");
    }

    public Game getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read game", ex);
        }
        throw new DataAccessException("Game not found");
    }

    public Map<Integer, Game> getAllGames() throws DataAccessException {
        var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games";
        Map<Integer, Game> games = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Game game = readGame(rs);
                        games.put(game.getGameID(), game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read games", ex);
        }
        return games;
    }

    public void updateGame(Game game) throws DataAccessException {
        var statement = "UPDATE games SET gameName = ?, whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";

        // Serialize ChessGame to JSON
        String gameJson = gson.toJson(game.getGame());

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, game.getGameName());
                preparedStatement.setString(2, game.getWhiteUserName());
                preparedStatement.setString(3, game.getBlackUserName());
                preparedStatement.setString(4, gameJson);
                preparedStatement.setInt(5, game.getGameID());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Game not found");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to update game", ex);
        }
    }

    public void clear() throws DataAccessException{
        var statement = "DELETE FROM games";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to clear games", ex);
        }
    }
    private Game readGame(ResultSet rs) throws SQLException {
        var game = new Game();
        game.setGameID(rs.getInt("gameID"));
        game.setGameName(rs.getString("gameName"));
        game.setWhiteUserName(rs.getString("whiteUsername"));
        game.setBlackUserName(rs.getString("blackUsername"));

        // Deserialize JSON string to ChessGame
        String gameJson = rs.getString("game");
        ChessGame chessGame = gson.fromJson(gameJson, ChessGame.class);
        game.setGame(chessGame);

        return game;
    }
}