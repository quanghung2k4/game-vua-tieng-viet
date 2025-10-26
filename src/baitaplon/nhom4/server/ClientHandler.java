package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

/**
 * ClientHandler chịu trách nhiệm giao tiếp với 1 client.
 * Ghi chú: sendMessage giờ bền hơn — nó không ném IOException ra ngoài nữa,
 * mà sẽ dọn dẹp connection và xoá handler khỏi server khi socket bị đóng.
 */
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
                Object obj;
                try {
                    obj = in.readObject();
                } catch (EOFException eof) {
                    // client chủ động đóng connection
                    System.out.println("🔴 Client ngắt kết nối (EOF): " + socket.getInetAddress());
                    break;
                } catch (IOException ioe) {
                    System.out.println("🔴 Lỗi đọc từ client: " + ioe.getMessage());
                    break;
                }

                if (obj instanceof MessageModel) {
                    MessageModel message = (MessageModel) obj;
                    if (message != null) {
                        try {
                            messageProcessor.process(message);
                        } catch (Exception e) {
                            // Bảo toàn vòng lặp — nếu xử lý message lỗi thì log và tiếp tục
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Nếu đọc được object không phải MessageModel, có thể bỏ qua hoặc log
                    System.out.println("⚠️ Nhận object không phải MessageModel: " + (obj != null ? obj.getClass() : "null"));
                }
            }

        } catch (Exception e) {
            System.out.println("🔴 Lỗi ClientHandler: " + e.getMessage());
            // e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Gửi Message đến client. Nếu gửi thất bại (socket đã đóng, connection reset, ...)
     * thì dọn dẹp connection và xoá handler khỏi server. Phương thức này KHÔNG ném exception.
     */
    public synchronized void sendMessage(MessageModel message) {
        if (out == null || socket == null || socket.isClosed()) {
            // Không còn kết nối, dọn dẹp
            closeConnection();
            return;
        }
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("🔴 Gửi message thất bại tới client " + (socket != null ? socket.getInetAddress() : "") + ": " + e.getMessage());
            // Dọn dẹp và loại bỏ handler khỏi server
            closeConnection();
        }
    }

    /**
     * Đóng kết nối an toàn, đóng streams, socket, và thông báo cho MainServer loại bỏ handler.
     */
    public synchronized void closeConnection() {
        if (!isRunning) {
            // đã dọn dẹp rồi
            return;
        }
        isRunning = false;
        try {
            if (in != null) {
                try { in.close(); } catch (IOException ignored) {}
            }
            if (out != null) {
                try { out.close(); } catch (IOException ignored) {}
            }
            if (socket != null && !socket.isClosed()) {
                try { socket.close(); } catch (IOException ignored) {}
            }
        } finally {
            // Thông báo server loại bỏ handler khỏi danh sách
            try {
                server.removeClientHandler(this);
            } catch (Exception ignored) {}
            System.out.println("🔴 Đã đóng connection và loại bỏ ClientHandler cho " + (user != null ? user.getUsername() : socket.getInetAddress()));
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}