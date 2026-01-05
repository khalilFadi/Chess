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

public class Server {
    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(authDAO, userDAO, gameDAO);

        registerEndpoints();
        registerExceptionHandlers();
        // Register your endpoints and exception handlers here.
    }
    private void registerEndpoints(){
        javalin.post("/user", ctx -> {
            RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
            LoginResponse response = userService.register(request);
            ctx.status(200);
            ctx.json(response);
        });
        javalin.post("/session", ctx -> {
            LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
            LoginResponse response = userService.login(request);
            ctx.status(200);
            ctx.json(response);
        });
        javalin.post("/game", ctx->{
            String authToken = ctx.header("authorization");
            CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameResponse response = gameService.createGame(request, authToken);
            ctx.status(200);
            ctx.json(response);
        });
        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(request, authToken);
            ctx.status(200);
        });
        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
        });
        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");
            ListGamesResponse response = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(response);
        });
        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.status(200);
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
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
