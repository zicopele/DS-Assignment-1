import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Handle incoming messages in a separate thread
            new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        System.out.println(response); // Print messages from the server
                    }
                } catch (IOException e) {
                    System.err.println("Connection lost to server.");
                }
            }).start();

            // User input for commands
            String username;
            System.out.println(in.readLine()); // Read the initial prompt
            username = scanner.nextLine();
            out.println(username); // Send the username to the server

            String command;
            while (true) {
                System.out.print("Enter command (ADD <amount> <detail>, VIEW or EXIT): ");
                command = scanner.nextLine();
                out.println(command); // Send the command to the server
                if (command.equals("EXIT")) {
                    break; // Exit the loop
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
