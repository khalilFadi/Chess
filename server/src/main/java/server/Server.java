package server;

import io.javalin.*;
import dataaccess.*;
import model.User;
import model.request.*;
import model.response.*;
import com.google.gson.Gson;

import service.ClearService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

public class Server {
    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Gson gson = new Gson();
    // private WebSocketHandler webSocketHandler;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e){
            throw new RuntimeException("unable to create database", e);
        }

        UserDAO userDAO;
        AuthDAO authDAO;
        GameDAO gameDAO;

        try {
            userDAO = new UserDAO();
            authDAO = new AuthDAO();
            gameDAO = new GameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Unable to initialize database tables", e);
        }
        // webSocketHandler = new WebSocketHandler(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(authDAO, userDAO, gameDAO);

        registerEndpoints();
        registerExceptionHandlers();
        // registerWebSocket();
        // Register your endpoints and exception handlers here.
    }
    // private void registerWebSocket(){
    //     javalin.ws("/ws", ws -> {
    //         ws.onMessage((ctx, message) -> {
    //             webSocketHandler.onMessage(ctx.session, message);
    //         });
    //     });
    // }
    private void registerEndpoints(){
        javalin.post("/user", ctx -> {
            try {
                RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
                LoginResponse response = userService.register(request);
                ctx.status(200);
                ctx.json(response);
            } catch (Exception e) {
                throw new DataAccessException("bad request");
            }
        });
        javalin.post("/session", ctx -> {
            try {
                LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
                LoginResponse response = userService.login(request);
                ctx.status(200);
                ctx.json(response);
            } catch (Exception e) {
                throw new DataAccessException("bad request");
            }
        });
        javalin.post("/game", ctx->{
            String authToken = ctx.header("authorization");
            try {
                CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
                CreateGameResponse response = gameService.createGame(request, authToken);
                ctx.status(200);
                ctx.json(response);
            } catch (DataAccessException e) {
                throw e;  // Re-throw to be handled by exception handler
            } catch (Exception e) {
                throw new DataAccessException("bad request");
            }
        });
        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(request, authToken);
            ctx.status(200);
            ctx.json(new java.util.HashMap<>());
        });
        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
            ctx.json(new java.util.HashMap<>());
        });
        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");
            try {
                ListGamesResponse response = gameService.listGames(authToken);
                ctx.status(200);
                ctx.json(response);
            } catch (DataAccessException e){
                throw e;
            } catch( Exception e){
                throw new DataAccessException("bad request");
            }
        });
        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.status(200);
            ctx.json(new java.util.HashMap<>());
        });
    }
    private void registerExceptionHandlers(){
        javalin.exception(DataAccessException.class, (e, ctx) -> {
            String message = e.getMessage();
            ErrorMessageResponse response = new ErrorMessageResponse("Error: " +message);
            if(message.equals("bad request")){
                ctx.status(400);
            }else if(message.equals("unauthorized") || message.equals("Invalid auth token")){
                ctx.status(401);
            }else if(message.equals("already taken")){
                ctx.status(403);
            }else{
                ctx.status(500);
            }
            ctx.json(response);
        });
        javalin.exception(Exception.class, (e, ctx) -> {
            ErrorMessageResponse response = new ErrorMessageResponse("Error: " + e.getMessage());
            ctx.status(500);
            ctx.json(response);
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
