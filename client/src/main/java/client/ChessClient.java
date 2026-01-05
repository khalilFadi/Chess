package client;

import chess.ChessGame;

import java.util.Scanner;

public class ChessClient {
    private final ServerFacade serverFacade;
    private final Scanner scanner = new Scanner(System.in);
    private String authToken = null;
    private boolean shouldQuit = false;

    public ChessClient(int serverPort) {
        this.serverFacade = new ServerFacade(serverPort);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type help");

        while (!shouldQuit) {
            if (authToken == null) {
                preloginUI();
            } else {
                postloginUI();
            }
        }
    }

    private void preloginUI() {
        System.out.print("\n[LOGGED_OUT] >>> ");
        String line = scanner.nextLine().trim();
        String[] tokens = line.split("\\s+");

        if (tokens.length == 0) {
            return;
        }

        String command = tokens[0].toLowerCase();

        try {
            switch (command) {
                case "help" -> printPreloginHelp();
                case "quit" -> {
                    System.out.println("Thanks for playing!");
                    shouldQuit = true;
                }
                case "login" -> handleLogin();
                case "register" -> handleRegister();
                default -> System.out.println("Unknown command. Type 'help' for available commands.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printPreloginHelp() {
        System.out.println();
        System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("  login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("  quit - playing chess");
        System.out.println("  help - with possible commands");
    }

    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Error: Username and password are required");
            return;
        }

        try {
            var response = serverFacade.login(username, password);
            authToken = response.getAuthToken();
            System.out.println("Logged in as " + response.getUsername());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleRegister() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            System.out.println("Error: Username, password, and email are required");
            return;
        }

        try {
            var response = serverFacade.register(username, password, email);
            authToken = response.getAuthToken();
            System.out.println("Registered and logged in as " + response.getUsername());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private java.util.Map<Integer, Integer> gameNumberToID = new java.util.HashMap<>();

    private void postloginUI() {
        System.out.print("\n[LOGGED_IN] >>> ");
        String line = scanner.nextLine().trim();
        String[] tokens = line.split("\\s+");

        if (tokens.length == 0) {
            return;
        }

        String command = tokens[0].toLowerCase();

        try {
                switch (command) {
                case "help" -> printPostloginHelp();
                case "logout" -> handleLogout();
                case "create" -> handleCreateGame();
                case "list" -> handleListGames();
                case "play" -> handlePlayGame();
                case "observe" -> handleObserveGame();
        default -> System.out.println("Unknown command. Type 'help' for available commands.");
    }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            }
            }

    private void printPostloginHelp() {
        System.out.println();
        System.out.println("  create <NAME> - a game");
        System.out.println("  list - games");
        System.out.println("  play <ID> [WHITE|BLACK] - a game");
        System.out.println("  observe <ID> - a game");
        System.out.println("  logout - when you are done");
        System.out.println("  quit - playing chess");
        System.out.println("  help - with possible commands");
    }

    private void handleLogout() {
        try {
            serverFacade.logout(authToken);
            authToken = null;
            gameNumberToID.clear();
            System.out.println("Logged out successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCreateGame() {
        System.out.print("Game name: ");
        String gameName = scanner.nextLine().trim();

        if (gameName.isEmpty()) {
            System.out.println("Error: Game name is required");
            return;
        }

        try {
            var response = serverFacade.createGame(authToken, gameName);
            System.out.println("Game created successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleListGames() {
        try {
            var response = serverFacade.listGames(authToken);
            var games = response.getGames();

            if (games == null || games.isEmpty()) {
                System.out.println("No games available");
                return;
            }

            gameNumberToID.clear();
            System.out.println();
            for (int i = 0; i < games.size(); i++) {
                int gameNumber = i + 1;
                var game = games.get(i);
                gameNumberToID.put(gameNumber, game.getGameID());

                String whitePlayer = game.getWhiteUsername() != null ? game.getWhiteUsername() : "none";
                String blackPlayer = game.getBlackUsername() != null ? game.getBlackUsername() : "none";

                System.out.println(gameNumber + ". " + game.getGameName() +
                        " | White: " + whitePlayer +
                        " | Black: " + blackPlayer);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handlePlayGame() {
        System.out.print("Which game number? ");
        String gameNumStr = scanner.nextLine().trim();

        int gameNumber;
        try {
            gameNumber = Integer.parseInt(gameNumStr);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid game number");
            return;
        }

        Integer gameID = gameNumberToID.get(gameNumber);
        if (gameID == null) {
            System.out.println("Error: Game number not found. Please list games first.");
            return;
        }

        System.out.print("Which color (WHITE/BLACK)? ");
        String colorStr = scanner.nextLine().trim().toUpperCase();

        chess.ChessGame.TeamColor playerColor;
        try {
            playerColor = chess.ChessGame.TeamColor.valueOf(colorStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid color. Must be WHITE or BLACK");
            return;
        }

        try {
            serverFacade.joinGame(authToken, gameID, playerColor);
            System.out.println("Joined game as " + colorStr);


            chess.ChessGame game = new chess.ChessGame();
            game.getBoard().resetBoard();
            ui.ChessBoardDrawer.drawBoard(game.getBoard(), playerColor);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleObserveGame() {
        System.out.print("Which game number? ");
        String gameNumStr = scanner.nextLine().trim();

        int gameNumber;
        try {
            gameNumber = Integer.parseInt(gameNumStr);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid game number");
            return;
        }

        Integer gameID = gameNumberToID.get(gameNumber);
        if (gameID == null) {
            System.out.println("Error: Game number not found. Please list games first.");
            return;
        }

        try {
            serverFacade.joinGame(authToken, gameID, null);
            System.out.println("Observing game");


            chess.ChessGame game = new chess.ChessGame();
            game.getBoard().resetBoard();
            ui.ChessBoardDrawer.drawBoard(game.getBoard(), ChessGame.TeamColor.WHITE);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}