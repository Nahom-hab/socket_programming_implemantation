import java.io.*;
import java.net.*;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            // User-friendly prompt message
            System.out.println("Welcome! You can use the following commands:");
            System.out.println("1. Time - Get the current time from the server.");
            System.out.println("2. Calculate <num1> <operator> <num2> - Perform a basic calculation.");
            System.out.println("   Example: Calculate 5 + 3");
            System.out.println("3. Random Fact - Get a random fact.");
            System.out.println("4. Exit - Close the connection.");
            System.out.println("Type your command below:");

            // Keep reading until "Exit" is input
            while (!line.equals("Exit")) {
                line = input.readLine().trim();

                // Make sure the user input is well formatted
                if (line.equalsIgnoreCase("Time")) {
                    out.writeUTF("Time");
                } else if (line.toLowerCase().startsWith("calculate")) {
                    // Parse the calculation command
                    String[] parts = line.split(" ");
                    if (parts.length == 4) {
                        try {
                            double num1 = Double.parseDouble(parts[1]);
                            String operator = parts[2];
                            double num2 = Double.parseDouble(parts[3]);

                            // Validate the operator
                            if (operator.matches("[+\\-*/]")) {
                                out.writeUTF(line);
                            } else {
                                System.out.println("Invalid operator. Please use +, -, *, or /.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid numbers in the calculation.");
                        }
                    } else {
                        System.out.println("Error: Invalid calculation format. Please use the format: Calculate <num1> <operator> <num2>");
                    }
                } else if (line.equalsIgnoreCase("Random Fact")) {
                    out.writeUTF("Random Fact");
                } else if (line.equalsIgnoreCase("Exit")) {
                    out.writeUTF("Exit");
                } else {
                    System.out.println("Error: Unknown command. Please use one of the following:\n" +
                            "1. Time\n" +
                            "2. Calculate <num1> <operator> <num2>\n" +
                            "3. Random Fact\n" +
                            "4. Exit");
                }

                // Wait for and display the server response
                String response = new DataInputStream(socket.getInputStream()).readUTF();
                System.out.println("Server Response: " + response);
            }

            // Close the connection
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000);
    }
}
