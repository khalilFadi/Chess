package client.model.request;

import chess.ChessGame;

public class JoinGameRequest {
    private ChessGame.TeamColor playerColor;
    private Integer gameID;

    public JoinGameRequest() {}

    public JoinGameRequest(Integer gameID, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}