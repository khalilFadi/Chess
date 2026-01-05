package dataaccess;

import model.Game;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    private final Map<Integer, Game> games = new HashMap<>();
    private int nextGameID = 1;

    public int createGame(String gameName) throws DataAccessException {
        Game game = new Game();
        game.setGameID(nextGameID);
        game.setGameName(gameName);
        game.setGame(new chess.ChessGame());
        games.put(nextGameID, game);
        return nextGameID++;
    }

    public Game getGame(int gameID) throws DataAccessException {
        Game game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found");
        }
        return game;
    }

    public Map<Integer, Game> getAllGames() {
        return new HashMap<>(games);
    }

    public void updateGame(Game game) throws DataAccessException {
        if (!games.containsKey(game.getGameID())) {
            throw new DataAccessException("Game not found");
        }
        games.put(game.getGameID(), game);
    }

    public void clear() {
        games.clear();
        nextGameID = 1;
    }
}