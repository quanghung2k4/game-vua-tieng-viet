package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private MainServer server;
    private DatabaseManager dbManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private volatile boolean isRunning = true;

    public ClientHandler(Socket socket, MainServer server, DatabaseManager dbManager) {
        this.socket = socket;
        this.server = server;
        this.dbManager = dbManager;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (isRunning) {
                MessageModel message = (MessageModel) in.readObject();
                if (message != null) {
                    handleMessage(message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void handleMessage(MessageModel message) throws IOException {
        switch (message.getType()) {
            case "request_login":
                handleLogin(message);
                break;
        }
    }

    private void handleLogin(MessageModel message) throws IOException {
        String[] tmp = message.getContent().split("\\|");
        MessageModel messLogin = new MessageModel("return_login", "OK");
        if (tmp[0].equals("a") && tmp[1].equals("1")) {
            
        } else {
            messLogin.setContent("FAIL");
        }
        sendMessage(messLogin);
    }

    private void sendMessage(MessageModel message) throws IOException {
        if (socket != null) {
            out.writeObject(message);
            out.flush();
        }
    }

}
