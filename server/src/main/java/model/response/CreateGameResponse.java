package model.response;

public class CreateGameResponse {
    private Integer gameID;

    public CreateGameResponse() {}

    public CreateGameResponse(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}