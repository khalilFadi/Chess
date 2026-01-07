package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import java.io.IOException;

public class WebSocketHandler{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final Gson gson = new Gson();

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO  = authDAO;
    }

    public void onMessage(Session session, String message){
        try{
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            System.out.println("Received command: " + command.getCommandType());
            switch (command.getCommandType()) {
                case CONNECT -> System.out.println("CONNECT command received");
                case MAKE_MOVE -> System.out.println("MAKE_MOVE command received");
                case LEAVE -> System.out.println("LEAVE command received");
                case RESIGN -> System.out.println("RESIGN command received");
            }
        } catch (Exception e) {
            System.out.println("Error handling message: " + e.getMessage());
        }

    }
}