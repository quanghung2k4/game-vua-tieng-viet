package baitaplon.nhom4.client.network;

import baitaplon.nhom4.client.controller.DashBoardController;
import baitaplon.nhom4.client.controller.LoginController;
import baitaplon.nhom4.client.model.MessageModel;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TCPClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        try {
            this.socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
            new Thread(this::listenServer).start();
            System.out.println("Kết nối server thành công: " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới server: " + e.getMessage());
            running = false;
        }
    }



    public void sendMessage(MessageModel message) throws IOException, ClassNotFoundException {
        out.writeObject(message);      // gửi Message
        out.flush();
    }

    public synchronized void connect() throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return;
        }
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }

    public synchronized Object sendMessage(MessageModel message) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            connect();
        }
        out.writeObject(message);
        out.flush();
        return in.readObject();
    }

    public synchronized boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized void close() {
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {}
        try {
            if (out != null) out.close();
        } catch (IOException ignored) {}
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        in = null;
        out = null;
        socket = null;
    }
}
