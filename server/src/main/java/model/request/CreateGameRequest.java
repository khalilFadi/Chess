package model.request;

public class CreateGameRequest {
    private String gameName;

    public CreateGameRequest() {}

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}