package model.request;

import chess.ChessGame;

public class JoinGameRequest {
    private ChessGame.TeamColor playerColor;
    private Integer gameID;

    public JoinGameRequest() {}

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