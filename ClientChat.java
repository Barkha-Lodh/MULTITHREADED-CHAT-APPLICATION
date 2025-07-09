import java.io.*;
import java.net.*;

public class ClientChat {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server!");

            
            System.out.print("Enter your name: ");
            String name = keyboard.readLine();
            //out.println(name); // send name to server

            Thread readThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            readThread.start();

            
            String message;
            while ((message = keyboard.readLine()) != null) {
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();
            System.out.println("Connection closed.");

        } catch (IOException e) {
            System.err.println("Unable to connect to server: " + e.getMessage());
        }
    }
}
