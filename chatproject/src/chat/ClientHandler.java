package chat;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;       // socket representing the connection to this client
    private ChatServer server;   // reference to the main server to call broadcast/remove
    private PrintWriter out;     // to send messages to this client

    public ClientHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void send (String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            // input stream to read messages from the client
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //output stream to send messages
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            //keep reading messages from the client until they disconnect
            while ((line = br.readLine()) != null) {
                System.out.println("Recieved: " + line);
                server.broadcast(line, this); // send message to all other clients
            }
        } catch (IOException e) {
            System.out.println("Connection lost");
        } finally {
            server.removeClient(this); // remove this client from servers list
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}