import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, List<String>> userExpenses = new HashMap<>(); // Store expenses by user
    private static Map<String, Double> userBalances = new HashMap<>();
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    // Main method
    public static void main(String[] args) {
        System.out.println("Budget Tracker Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Synchronized methods
    private static synchronized void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    private static synchronized void addUser(String username, PrintWriter out) {
        userBalances.put(username, 0.0);
        userExpenses.put(username, new ArrayList<>()); // Initialize expenses list for the user
        clientWriters.add(out);
        broadcast("User " + username + " has joined the budget tracker.");
        System.out.println("User " + username + " has joined the budget tracker."); // Log on server
        displayBalances();
    }

    private static synchronized void removeUser(String username, PrintWriter out) {
        userBalances.remove(username);
        userExpenses.remove(username); // Remove the user's expenses
        clientWriters.remove(out);
        broadcast("User " + username + " has left the budget tracker."); // Notify others
        System.out.println("User " + username + " has left the budget tracker."); // Log on server
        displayBalances();
    }

    // Add expense for a user
    private static synchronized void addExpense(String username, double amount, String detail) {
        userBalances.put(username, userBalances.get(username) + amount);
        userExpenses.get(username).add("Added expense of $" + amount + " for: " + detail);
        broadcast(username + " added an expense of $" + amount + " for: " + detail);
        displayBalances();
    }

    // View all expenses
    private static synchronized void viewAllExpenses(PrintWriter out) {
        if (userExpenses.isEmpty()) {
            out.println("No expenses recorded on the server.");
            return;
        }

        // Display all expenses for each user
        out.println("Summary of all expenses recorded on the server:");
        for (Map.Entry<String, List<String>> entry : userExpenses.entrySet()) {
            String user = entry.getKey();
            List<String> expenses = entry.getValue();
            out.println("Expenses for " + user + ":");
            if (expenses.isEmpty()) {
                out.println("  No expenses recorded.");
            } else {
                for (String expense : expenses) {
                    out.println("  " + expense);
                }
            }
        }
    }

    // Display balances for all users
    private static synchronized void displayBalances() {
        StringBuilder balanceReport = new StringBuilder("Current Balances:\n");
        for (Map.Entry<String, Double> entry : userBalances.entrySet()) {
            balanceReport.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
        }
        broadcast(balanceReport.toString());
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request and add user
                out.println("Enter your username:");
                username = in.readLine();
                addUser(username, out); // Pass out to addUser

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("ADD ")) {
                        try {
                            String[] parts = input.split(" ", 3); // Split into 3 parts: ADD, amount, detail
                            if (parts.length < 3) {
                                out.println("Invalid command. Use: ADD <amount> <detail>");
                                continue;
                            }
                            double amount = Double.parseDouble(parts[1]);
                            String detail = parts[2];
                            addExpense(username, amount, detail);
                        } catch (NumberFormatException e) {
                            out.println("Invalid amount. Use: ADD <amount> <detail>");
                        }
                    } else if (input.equals("VIEW")) {
                        viewAllExpenses(out); // Show all expenses
                    } else if (input.equals("EXIT")) {
                        break; // Exit the loop
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                removeUser(username, out); // Pass out to removeUser
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
