import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started. Waiting for a client...");

            socket = server.accept();
            System.out.println("Client connected");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            while (!line.equals("Exit")) {
                line = in.readUTF();
                System.out.println("Received command: " + line);

                if (line.equalsIgnoreCase("Time")) {
                    // Send the current time to the client
                    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    out.writeUTF("Current server time: " + time);
                } else if (line.toLowerCase().startsWith("calculate")) {
                    // Handle calculations (e.g., Calculate 5 + 3)
                    try {
                        String[] parts = line.split(" ");
                        double num1 = Double.parseDouble(parts[1]);
                        String operator = parts[2];
                        double num2 = Double.parseDouble(parts[3]);
                        double result = 0;

                        switch (operator) {
                            case "+":
                                result = num1 + num2;
                                break;
                            case "-":
                                result = num1 - num2;
                                break;
                            case "*":
                                result = num1 * num2;
                                break;
                            case "/":
                                if (num2 != 0) {
                                    result = num1 / num2;
                                } else {
                                    out.writeUTF("Error: Division by zero.");
                                    continue;
                                }
                                break;
                            default:
                                out.writeUTF("Error: Unsupported operator.");
                                continue;
                        }
                        out.writeUTF("Result: " + result);
                    } catch (Exception e) {
                        out.writeUTF("Error: Invalid calculation format.");
                    }
                } else if (line.equalsIgnoreCase("Random Fact")) {
                    // Send a random fact
                    String fact = "Did you know? Honey never spoils.";
                    out.writeUTF("Random Fact: " + fact);
                } else if (line.equalsIgnoreCase("Exit")) {
                    out.writeUTF("Exiting the server. Goodbye!");
                } else {
                    out.writeUTF("Unknown command. Please use one of the following:\n" +
                            "type \'Time\' - Get current server time\n" +
                            "type \'Calculate <num1> <operator> <num2>\' - Perform a calculation\n" +
                            "type \'Random Fact\' - Get a random fact\n" +
                            "4. Exit - Exit the program");
                }
            }
            System.out.println("Closing connection.");
            socket.close();
            in.close();
            out.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}
