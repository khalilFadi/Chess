package  model;
import chess.ChessGame;

public class Game{
    private int gameID;
    private String gameName;
    private String whiteUserName;
    private String blackUserName;
    private ChessGame game;

    public Game(){}

    public Game(int gameID, String gameName, String whiteUserName, String blackUserName, ChessGame game){
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.game = game;
    }
    public int getGameID(){
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public void setWhiteUserName(String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }

    public void setBlackUserName(String blackUserName) {
        this.blackUserName = blackUserName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}