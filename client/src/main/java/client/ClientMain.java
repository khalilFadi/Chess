package client;

public class ClientMain {
    public static void main(String[] args) {
        int serverPort = 8080;
        if (args.length > 0) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default 8080.");
            }
        }

        var client = new ChessClient(serverPort);
        client.run();
    }
}