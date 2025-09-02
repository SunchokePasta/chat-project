package chat;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    // Thread-safe set of connected clients
    private Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws IOException {
        int port = 5000;
        new ChatServer().start(port);
        
    }

    // Starts the server and listens for client connections
    public void start(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server running on port " + port);

        // keep accepting clients indefinitely
        while (true) {
            Socket socket = server.accept(); // Waits until a client connects
            System.out.println("A client connected!");

            // create a handler for a new client

            ClientHandler handler = new ClientHandler(this, socket);

            // add to the set of active clients
            clients.add(handler);

            // start a new thread to handle this client
            new Thread(handler).start();

        }
    }

    // send a message to all clients except the sender
    public void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler c : clients) {
            if (c != sender) {
                    c.send(msg);
            }
        }
    }

    // remove a client from the active set
    public void removeClient(ClientHandler c) {
        clients.remove(c);
    }
}