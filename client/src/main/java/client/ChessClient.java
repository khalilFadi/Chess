package client;

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

    private void postloginUI() {
        // later
    }
}