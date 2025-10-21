package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import java.io.*;
import java.net.*;
import java.sql.Connection;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final MainServer server;
    private final Connection conn;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private volatile boolean isRunning = true;
    private MessageProcessor messageProcessor;
    private User user;

    public ClientHandler(Socket socket, MainServer server, Connection conn) {
        this.socket = socket;
        this.server = server;
        this.conn = conn;
        this.messageProcessor = new MessageProcessor(this, conn);
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("🟢 Client connected: " + socket.getInetAddress());

            while (isRunning) {
                MessageModel message = (MessageModel) in.readObject();
                if (message != null) {
                    messageProcessor.process(message);
                }
            }

        } catch (EOFException e) {
            System.out.println("🔴 Client ngắt kết nối: " + socket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(MessageModel message) throws IOException {
        if (out != null) {
            out.writeObject(message);
            out.flush();
        }
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public User getUser() {
    return user;
}

    public void setUser(User user) {
        this.user = user;
    }
}
