package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.shared.game.WordBatchDTO;
import baitaplon.nhom4.server.service.UserService;

import java.io.IOException;
import java.sql.Connection;

public class MessageProcessor {

    private final ClientHandler client;
    private final Connection conn;
    private final UserService userService;

    public MessageProcessor(ClientHandler client, Connection conn) {
        this.client = client;
        this.conn = conn;
        this.userService = new UserService(conn);
        try {
            GameWordService.init("data/clean.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case "request_invite_player":
                handleInvitePlayer(message);
                break;
            case "response_invite":
                handleResponseInvite(message);
                break;
            case "request_word_batch": {
                WordBatchDTO batch = GameWordService.generateBatch(30, 5, 10);
                client.sendMessage(new MessageModel("return_word_batch", batch));
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
        System.out.println(result);
        if (result.equals("OK")) {
            User loggedInUser = userService.getUserByUserName(username);
            // Gán user đó vào client hiện tại
            client.setUser(loggedInUser);
        }
        System.out.println("sent");
        client.sendMessage(new MessageModel("return_login", result));
        System.out.println("sented");
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
//        System.out.println("📩 Nhận yêu cầu lấy danh sách người chơi");
        try {
            String playersData = userService.getAllPlayers();
            client.sendMessage(new MessageModel("return_get_players", playersData));
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách người chơi: " + e.getMessage());
            client.sendMessage(new MessageModel("return_player_list", "ERROR|Không thể lấy danh sách người chơi"));
        }
    }

    private void handleInvitePlayer(MessageModel message) throws IOException {
        try {
            // content dạng: "senderUsername|receiverUsername"
            String[] parts = message.getContent().split("\\|");
            if (parts.length != 2) {
                client.sendMessage(new MessageModel("invite_error", "Dữ liệu lời mời không hợp lệ."));
                return;
            }

            String senderUsername = parts[0];
            String receiverUsername = parts[1];

            // Tìm người gửi & người nhận trong database
            User sender = userService.getUserByUserName(senderUsername);
            User receiver = userService.getUserByUserName(receiverUsername);
            //Tìm client của người nhận (đang online)
            ClientHandler receiverClient = MainServer.getClientHandlerByUserName(receiver.getUsername());
            if(receiverClient == null) {
                client.sendMessage(new MessageModel("invite_error", "Người chơi "+receiver.getDisplayName()+" không trực tuyến"));
            }

            System.out.println(receiverClient.getUser().getUsername() +" ");
            // Gửi lời mời tới người nhận
            MessageModel inviteMsg = new MessageModel();
            inviteMsg.setType("receive_invite");
            inviteMsg.setContent(sender.getUsername() +","+sender.getDisplayName()+ "|" + receiver.getUsername());

            receiverClient.sendMessage(inviteMsg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            client.sendMessage(new MessageModel("invite_error", "Lỗi khi xử lý lời mời: " + e.getMessage()));
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
            User receiver = userService.getUserByUserName(receiverUsername);
            System.out.println(senderUsername+" "+receiverUsername);
            ClientHandler senderClient = MainServer.getClientHandlerByUserName(senderUsername);
            System.out.println("Gưi ve "+ senderClient.getUser().getUsername());
            if (senderClient == null) {
                client.sendMessage(new MessageModel("invite_error", "Người mời không còn trực tuyến."));
                return;
            }

            MessageModel reply = new MessageModel();
            reply.setType("invite_result");
            reply.setContent(receiverUsername + "|" +receiver.getDisplayName()+"|"+ response);

            senderClient.sendMessage(reply);
        } catch (Exception e) {
            e.printStackTrace();
            client.sendMessage(new MessageModel("invite_error", "Lỗi khi xử lý phản hồi lời mời: " + e.getMessage()));
        }
    }
}
