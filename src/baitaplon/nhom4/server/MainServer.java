package baitaplon.nhom4.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class MainServer {
    private static final int PORT = 3636;
    private ServerSocket serverSocket;
    private Connection conn; // ✅ Đổi kiểu này về Connection

    public MainServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            conn = DatabaseManager.getConnection(); // ✅ Gọi hàm static lấy connection
            System.out.println("✅ Server đang chạy ở port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("📡 Client mới kết nối: " + client.getInetAddress());

                // Truyền connection vào ClientHandler
                ClientHandler clientHandler = new ClientHandler(client, this, conn);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MainServer();
    }
}
