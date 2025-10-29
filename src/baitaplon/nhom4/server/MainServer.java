package baitaplon.nhom4.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainServer {
    private static final int PORT = 3636;
    private ServerSocket serverSocket;
    private Connection conn;
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();

    public MainServer() {
        try {
            WordDictionary.loadFromFile("data/clean.txt");
            serverSocket = new ServerSocket(PORT);
            conn = DatabaseManager.getConnection();
            System.out.println("Server đang chạy ở port " + PORT);
            conn = DatabaseManager.getConnection(); //Gọi hàm static lấy connection


            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client mới kết nối: " + client.getInetAddress());

                // Truyền connection vào ClientHandler
                ClientHandler clientHandler = new ClientHandler(client, this, conn);
                synchronized (clientHandlers) {
                    clientHandlers.add(clientHandler);
                }
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // cleanup server socket nếu cần
            try {
                if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            } catch (Exception ignored) {}
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

    public void removeClientHandler(ClientHandler handler) {
        synchronized (clientHandlers) {
            // Dùng iterator để an toàn khi xóa
            Iterator<ClientHandler> it = clientHandlers.iterator();
            while (it.hasNext()) {
                ClientHandler h = it.next();
                if (h == handler) {
                    it.remove();
                    System.out.println("🗑️ ClientHandler removed: " + (handler.getUser() != null ? handler.getUser().getUsername() : "unknown"));
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new MainServer();
    }
}