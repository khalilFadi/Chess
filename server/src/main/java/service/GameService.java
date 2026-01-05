package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameService{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void verifyAuth(String authToken) throws DataAccessException{
        authDAO.getUsername(authToken);
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws DataAccessException{
        verifyAuth(authToken);
        if(request.getGameName() == null){
            throw new DataAccessException("bad request");
        }

        int gameID = gameDAO.createGame(request.getGameName());
        return new CreateGameResponse(gameID);
    }
    public ListGamesResponse listGames(String authToken) throws DataAccessException{
        verifyAuth(authToken);
        Collection<Game> games = gameDAO.getAllGames().values();
        List<ListGamesResponse.GameListItem> gameList = new ArrayList<>();

        for(Game game : games){
            gameList.add(new ListGamesResponse.GameListItem(
                    game.getGameID(),
                    game.getGameName(),
                    game.getWhiteUserName(),
                    game.getBlackUserName()
            ));
        }
        return new ListGamesResponse(gameList);
    }
    public void joinGame(JoinGameRequest request, String authToken) throws DataAccessException{
        String username = authDAO.getUsername(authToken);
        if(request.getGameID() == null || request.getPlayerColor() == null){
            throw new DataAccessException("bad request");
        }
        Game game = gameDAO.getGame(request.getGameID());
        ChessGame.TeamColor color = request.getPlayerColor();
        if(color == ChessGame.TeamColor.WHITE){
            if(game.getWhiteUserName() != null && !game.getWhiteUserName().equals(username)){
                throw new DataAccessException("already taken");
            }
            game.setWhiteUserName(username);
        }else if(color == ChessGame.TeamColor.BLACK){
            if(game.getBlackUserName() != null && !game.getBlackUserName().equals(username)){
                throw new DataAccessException("already taken");
            }
            game.setBlackUserName(username);
        }
        gameDAO.updateGame(game);
    }
}