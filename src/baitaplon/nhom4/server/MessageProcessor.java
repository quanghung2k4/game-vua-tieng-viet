package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.service.UserService;
import baitaplon.nhom4.server.service.LeaderboardService;

import java.io.IOException;
import java.sql.Connection;

public class MessageProcessor {

    private final ClientHandler client;
    private final Connection conn;
    private final UserService userService;
    private final LeaderboardService leaderboardService;

    public MessageProcessor(ClientHandler client, Connection conn) {
        this.client = client;
        this.conn = conn;
        this.userService = new UserService(conn);
        this.leaderboardService = new LeaderboardService(conn);
    }

    public void process(MessageModel message) throws IOException {
        switch (message.getType()) {
            case "request_login":
                handleLogin(message);
                break;
            case "request_register":
                handleRegister(message);
                break;
            case "request_player_list":
                handleGetPlayers(message);
                break;
            case "request_leaderboard":
                handleGetLeaderboard(message);
                break;
            case "request_logout":
                handleLogout(message);
                break;
            default:
                System.out.println("⚠️ Loại message chưa hỗ trợ: " + message.getType());
        }
    }

    private void handleLogin(MessageModel message) throws IOException {
        System.out.println("📩 Nhận yêu cầu login: " + message.getContent());
        String[] tmp = message.getContent().split("\\|");
        if (tmp.length < 2) {
            client.sendMessage(new MessageModel("return_login", "SAI_DINH_DANG"));
            return;
        }

        String username = tmp[0];
        String password = tmp[1];
        String result = userService.checkLogin(username, password);
        client.sendMessage(new MessageModel("return_login", result));
    }

    private void handleRegister(MessageModel message) throws IOException {
        System.out.println("📩 Nhận yêu cầu đăng ký: " + message.getContent());
        String[] tmp = message.getContent().split("\\|");
        if (tmp.length < 2) {
            client.sendMessage(new MessageModel("return_register", "SAI_DINH_DANG"));
            return;
        }
        User newUser = new User(tmp[0], tmp[1], tmp[2]);

        String result = userService.registerUser(newUser);
        client.sendMessage(new MessageModel("return_register", result));
    }

    private void handleGetPlayers(MessageModel message) throws IOException {
        System.out.println("📩 Nhận yêu cầu lấy danh sách người chơi");
        try {
            String playersData = userService.getAllPlayers();
            client.sendMessage(new MessageModel("return_player_list", playersData));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách người chơi: " + e.getMessage());
            client.sendMessage(new MessageModel("return_player_list", "ERROR|Không thể lấy danh sách người chơi"));
        }
    }

    private void handleGetLeaderboard(MessageModel message) throws IOException {
        System.out.println("📩 Nhận yêu cầu lấy bảng xếp hạng");
        try {
            String leaderboardData = leaderboardService.getLeaderboard();
            client.sendMessage(new MessageModel("return_leaderboard", leaderboardData));
            System.out.println("✅ Đã gửi bảng xếp hạng thành công");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy bảng xếp hạng: " + e.getMessage());
            client.sendMessage(new MessageModel("return_leaderboard", "ERROR|Không thể lấy bảng xếp hạng"));
        }
    }

    private void handleLogout(MessageModel message) throws IOException {
        System.out.println("📩 Nhận yêu cầu logout: " + message.getContent());
        String username = message.getContent();
        
        if (username == null || username.trim().isEmpty()) {
            client.sendMessage(new MessageModel("return_logout", "SAI DINH DANG"));
            return;
        }
        
        String result = userService.logout(username.trim());
        client.sendMessage(new MessageModel("return_logout", result));
        
        if ("OK".equals(result)) {
            System.out.println("✅ User " + username + " đã logout thành công");
        }
    }
}
