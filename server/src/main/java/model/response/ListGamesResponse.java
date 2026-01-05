package model.response;

import java.util.List;

public class ListGamesResponse {
    private List<GameListItem> games;

    public ListGamesResponse() {}

    public ListGamesResponse(List<GameListItem> games) {
        this.games = games;
    }

    public List<GameListItem> getGames() {
        return games;
    }

    public void setGames(List<GameListItem> games) {
        this.games = games;
    }

    public static class GameListItem {
        private int gameID;
        private String gameName;
        private String whiteUsername;
        private String blackUsername;

        public GameListItem() {}

        public GameListItem(int gameID, String gameName, String whiteUsername, String blackUsername) {
            this.gameID = gameID;
            this.gameName = gameName;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
        }

        public int getGameID() {
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

        public String getWhiteUsername() {
            return whiteUsername;
        }

        public void setWhiteUsername(String whiteUsername) {
            this.whiteUsername = whiteUsername;
        }

        public String getBlackUsername() {
            return blackUsername;
        }

        public void setBlackUsername(String blackUsername) {
            this.blackUsername = blackUsername;
        }
    }
}