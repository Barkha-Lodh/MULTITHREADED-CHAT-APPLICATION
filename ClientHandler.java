import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Set<ClientHandler> clientHandlers;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            out.println("Enter your name:");
            clientName = in.readLine();
            broadcast(clientName + " has joined the chat.");

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(clientName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println(clientName + " disconnected.");
        } finally {
            try {
                clientHandlers.remove(this);
                broadcast(clientName + " has left the chat.");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                handler.out.println(message);
            }
        }
    }
}
