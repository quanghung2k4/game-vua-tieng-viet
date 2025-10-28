package baitaplon.nhom4.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
    private static final int PORT = 3636;
    private ServerSocket serverSocket;
    private Connection conn;
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();

    public MainServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            conn = DatabaseManager.getConnection();
            System.out.println("✅ Server đang chạy ở port " + PORT);
            conn = DatabaseManager.getConnection(); //Gọi hàm static lấy connection
            System.out.println("Server đang chạy ở port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client mới kết nối: " + client.getInetAddress());

                // Truyền connection vào ClientHandler
                ClientHandler clientHandler = new ClientHandler(client, this, conn);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ClientHandler getClientHandlerByUserName(String username) {
    synchronized (clientHandlers) {
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUser() != null && handler.getUser().getUsername().equals(username)) {
                return handler;
            }
        }
    }
    return null;
}
    public static void main(String[] args) {
        new MainServer();
    }
}
