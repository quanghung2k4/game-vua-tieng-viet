package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.service.UserService;
import baitaplon.nhom4.server.service.LeaderboardService;
import baitaplon.nhom4.shared.game.GameStartDTO;
import baitaplon.nhom4.shared.game.WordBatchDTO;

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
            case "request_invite_player":
                handleInvitePlayer(message);
                break;
            case "request_history":
                handleHistory(message);
                break;
            case "invite_cancel":
                handleInviteCancel(message);
                break;
            case "response_invite":
                handleResponseInvite(message);
                break;
            case "invite_accept": {
                String[] parts = (message.getContent() == null ? "" : message.getContent()).split("\\|");
                if (parts.length >= 2) {
                    String inviter = parts[0];
                    String invitee = parts[1];
                    startGameForUsers(inviter, invitee);
                }
                break;
            }
            case "game_word_correct": {
                // content: "scorerUsername|opponentUsername"
                String[] parts = (message.getContent() == null ? "" : message.getContent()).split("\\|");
                String scorer = parts.length > 0 ? parts[0] : null;
                String opponent = parts.length > 1 ? parts[1] : null;
                if (opponent != null) {
                    ClientHandler opp = MainServer.getClientHandlerByUserName(opponent);
                    if (opp != null) {
                        MessageModel notify = new MessageModel("opponent_scored", scorer);
                        opp.sendMessage(notify);
                    }
                }
                break;
            }
            case "finish_game": {
                String[] parts = (message.getContent() == null ? "" : message.getContent()).split("\\|");
                String p1 = parts.length > 0 ? parts[0] : null;
                String p2 = parts.length > 1 ? parts[1] : null;
                String p1Score = parts.length > 2 ? parts[2] : null;
                String p2Score = parts.length >  3 ? parts[3] : null;
                String reason = parts.length > 4 ? parts[4] : null;
                System.out.println(p1 + " vs " + p2);
                GameSessionManager.finishGame(p1, p2, p1Score, p2Score, reason);
                break;
            }
            case "player_out": {
                // content: "loserUsername|opponentUsername" (opponent optional)
                String[] parts = (message.getContent() == null ? "" : message.getContent()).split("\\|");
                String loser = parts.length > 0 ? parts[0] : (client.getUser() != null ? client.getUser().getUsername() : null);
                GameSessionManager.playerOut(loser);
                break;
            }
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
        if (result.equals("OK")) {
            User loggedInUser = userService.getUserByUserName(username);
            client.setUser(loggedInUser);
            handleRefreshPlayer(username);
        }
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
            client.sendMessage(new MessageModel("return_get_players", playersData));
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
            handleRefreshPlayer(username);
        }
    }

    private void handleInvitePlayer(MessageModel message) throws IOException {
        try {
            String[] parts = message.getContent().split("\\|");
            if (parts.length != 2) {
                client.sendMessage(new MessageModel("invite_error", "Dữ liệu lời mời không hợp lệ."));
                return;
            }

            String senderUsername = parts[0];
            String receiverUsername = parts[1];

            User sender = userService.getUserByUserName(senderUsername);
            User receiver = userService.getUserByUserName(receiverUsername);

            ClientHandler receiverClient = MainServer.getClientHandlerByUserName(receiver.getUsername());
            if (receiverClient == null) {
                client.sendMessage(new MessageModel("invite_error", "Người chơi " + receiver.getDisplayName() + " không trực tuyến"));
                return;
            }

            MessageModel inviteMsg = new MessageModel();
            inviteMsg.setType("receive_invite");
            inviteMsg.setContent(sender.getUsername() + "," + sender.getDisplayName() + "|" + receiver.getUsername());
            receiverClient.sendMessage(inviteMsg);
        } catch (Exception e) {
            client.sendMessage(new MessageModel("invite_error", "Lỗi khi xử lý lời mời: " + e.getMessage()));
        }
    }

    private void handleInviteCancel(MessageModel message) throws IOException {
        try {
            String[] parts = message.getContent().split("\\|");

            String senderUsername = parts[0];
            String receiverUsername = parts[1];

            User sender = userService.getUserByUserName(senderUsername);
            User receiver = userService.getUserByUserName(receiverUsername);

            ClientHandler receiverClient = MainServer.getClientHandlerByUserName(receiver.getUsername());
            if (receiverClient == null) {

                return;
            }

            MessageModel inviteMsg = new MessageModel();
            inviteMsg.setType("invite_cancel");
            inviteMsg.setContent(sender.getUsername() + "," + sender.getDisplayName() + "|" + receiver.getUsername());
            receiverClient.sendMessage(inviteMsg);
        } catch (Exception e) {

        }
    }

    private void handleResponseInvite(MessageModel message) throws IOException {
        try {
            String[] parts = message.getContent().split("\\|");
            if (parts.length != 3) {
                client.sendMessage(new MessageModel("invite_error", "Dữ liệu phản hồi không hợp lệ."));
                return;
            }

            String senderUsername = parts[0];
            String receiverUsername = parts[1];
            String response = parts[2];

            ClientHandler senderClient = MainServer.getClientHandlerByUserName(senderUsername);
            if (senderClient == null) {
                client.sendMessage(new MessageModel("invite_error", "Người mời không còn trực tuyến."));
                return;
            }

            // Giữ behavior cũ để client cũ vẫn hiển thị
            MessageModel reply = new MessageModel();
            reply.setType("invite_result");
            // format: receiverUsername|receiverDisplayName|response
            User receiver = userService.getUserByUserName(receiverUsername);
            reply.setContent(senderUsername + "|" + receiverUsername + "|" + receiver.getDisplayName() + "|" + response);

            senderClient.sendMessage(reply);

            // Nếu đồng ý, khởi tạo ván đấu luôn (đảm bảo không cần thông báo "đã chấp nhận")
            if ("respone_accept".equals(response)) {
                startGameForUsers(senderUsername, receiverUsername);
            }

        } catch (Exception e) {
            client.sendMessage(new MessageModel("invite_error", "Lỗi khi xử lý phản hồi lời mời: " + e.getMessage()));
        }
    }

    private void startGameForUsers(String userA, String userB) throws IOException {
        long startAt = System.currentTimeMillis() + 3500;
        WordBatchDTO batch = GameWordService.generateBatch(30, 5, 10);

        System.out.println("startGameForUsers: " + userA + " " + userB);
        User user1 = userService.getUserByUserName(userA);
        User user2 = userService.getUserByUserName(userB);

        GameStartDTO dtoAB = new GameStartDTO(userA, userA, userB, user1.getDisplayName(), user2.getDisplayName(), batch, startAt, 3, 120);
        GameStartDTO dtoBA = new GameStartDTO(userA, userB, userA, user2.getDisplayName(), user1.getDisplayName(), batch, startAt, 3, 120);

        MessageModel startA = new MessageModel("game_start", dtoAB);
        MessageModel startB = new MessageModel("game_start", dtoBA);

        ClientHandler hA = MainServer.getClientHandlerByUserName(userA);
        ClientHandler hB = MainServer.getClientHandlerByUserName(userB);

        if (hA != null) {
            hA.sendMessage(startA);
        }
        if (hB != null) {
            hB.sendMessage(startB);
        }

        // Đăng ký cặp đang thi đấu để xử lý thoát/mất kết nối
        GameSessionManager.registerPair(userA, userB);
    }

    private void handleRefreshPlayer(String username) {
        for (ClientHandler clientHandler : MainServer.getClientHandlers()) {
            if (!clientHandler.getUser().getUsername().equals(username)) {
                MessageModel messRefresh = new MessageModel("refresh_player", "");
                clientHandler.sendMessage(messRefresh);
            }

        }
    }

    private void handleHistory(MessageModel message) {
        String ans = userService.getHistoryByUserName(message.getContent());
        MessageModel messageModel = new MessageModel("return_history",ans);
        client.sendMessage(messageModel);
    }
}
