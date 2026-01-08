package model.request;

import chess.ChessGame;

public class JoinGameRequest {
    private String playerColor;
    private Integer gameID;

    public JoinGameRequest() {}

    public ChessGame.TeamColor getPlayerColor() {
        if(playerColor == null || playerColor.isEmpty()){
            return null;
        }
        try{
            return ChessGame.TeamColor.valueOf(playerColor.toUpperCase());
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}