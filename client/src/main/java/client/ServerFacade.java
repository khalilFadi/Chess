package client;

import chess.ChessGame;
import client.model.request.*;
import client.model.response.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public LoginResponse register(String username, String password, String email) throws Exception {
        var path = "/user";
        RegisterRequest request = new RegisterRequest(username, password, email);
        return makeRequest("POST", path, null, request, LoginResponse.class);
    }

    public LoginResponse login(String username, String password) throws Exception {
        var path = "/session";
        LoginRequest request = new LoginRequest(username, password);
        return makeRequest("POST", path, null, request, LoginResponse.class);
    }

    public void logout(String authToken) throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, authToken, null, null);
    }

    public CreateGameResponse createGame(String authToken, String gameName) throws Exception {
        var path = "/game";
        CreateGameRequest request = new CreateGameRequest(gameName);
        return makeRequest("POST", path, authToken, request, CreateGameResponse.class);
    }

    public ListGamesResponse listGames(String authToken) throws Exception {
        var path = "/game";
        return makeRequest("GET", path, authToken, null, ListGamesResponse.class);
    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws Exception {
        var path = "/game";
        JoinGameRequest request = new JoinGameRequest(gameID, playerColor);
        makeRequest("PUT", path, authToken, request, null);
    }

    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = new URL(serverUrl + path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }

            if (request != null) {
                http.setRequestProperty("Content-Type", "application/json");
                try (OutputStream reqBody = http.getOutputStream()) {
                    String json = gson.toJson(request);
                    reqBody.write(json.getBytes());
                }
            }

            http.connect();

            int responseCode = http.getResponseCode();
            InputStream responseStream = (responseCode == HttpURLConnection.HTTP_OK)
                    ? http.getInputStream()
                    : http.getErrorStream();

            if (responseStream == null) {
                throw new Exception("No response from server");
            }

            try (InputStreamReader reader = new InputStreamReader(responseStream)) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    if (responseClass == null) {
                        return null; }
                    return gson.fromJson(reader, responseClass);
                } else {
                    ErrorMessageResponse errorResponse = gson.fromJson(reader, ErrorMessageResponse.class);
                    throw new Exception(errorResponse != null && errorResponse.getMessage() != null
                            ? errorResponse.getMessage()
                            : "Server returned error code: " + responseCode);
                }
            }
        } catch (IOException e) {
            throw new Exception("Failed to connect to server: " + e.getMessage(), e);
        }
    }
    public void clear() throws Exception{
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }
}