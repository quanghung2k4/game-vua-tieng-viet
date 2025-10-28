package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.model.PlayerData;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.view.DashBoard;
import baitaplon.nhom4.client.component.HomeForm;
import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.swing.GlassPanePopup;
import baitaplon.nhom4.client.view.GameScreen;
import baitaplon.nhom4.shared.game.GameStartDTO;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller cho Dashboard - xử lý việc lấy danh sách người chơi từ server
 */
public class DashBoardController {

    private final DashBoard view;
    private final TCPClient client;
    private HomeForm homeForm;
    private GameScreen currentGameScreen;
    private Timer refreshTimer;
    private boolean isRunning = false;
    private String username;
    private ModelPlayer opponentPlayer;

    public DashBoardController(String username, DashBoard view, TCPClient client){
        this.view = view;
        this.client = client;
        this.username = username;
    }

    /**
     * Bắt đầu lấy danh sách người chơi và cập nhật định kỳ
     */
    public void startPlayerListRefresh() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        // Lấy danh sách ngay lập tức
        fetchPlayerList();

        // Tạo timer để cập nhật mỗi 1 phút (60 giây)
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    fetchPlayerList();
                }
            }
        }, 60000, 60000); // Delay 60s, repeat every 60s
    }

    /**
     * Dừng việc cập nhật định kỳ
     */
    public void stopPlayerListRefresh() {
        isRunning = false;
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null;
        }
    }

    /**
     * Gửi request lấy danh sách người chơi từ server
     */
    private void fetchPlayerList() {
        new Thread(() -> {
            try {
                // Tạo request message
                MessageModel request = new MessageModel("request_player_list", "");

                // Gửi request đến server
                client.sendMessage(request);

            } catch (Exception ex) {
                // Xử lý lỗi kết nối
                SwingUtilities.invokeLater(() -> {
                    handleConnectionError(ex);
                });
            }
        }).start();
    }

    /**
     * Xử lý response từ server
     */
    public void handlePlayerListResponse(MessageModel response) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                System.err.println("Không nhận được phản hồi từ server!");
                return;
            }

            String responseContent = response.getContent();
//            System.out.println("Server response: " + responseContent);

            try {

                // Parse string response thành danh sách PlayerData
                List<PlayerData> playerList = parsePlayerListFromString(responseContent);

                // Cập nhật UI với danh sách mới
                updatePlayerListUI(playerList);

            } catch (Exception e) {
                System.err.println("Lỗi parse string: " + e.getMessage());
                // Fallback: tạo dữ liệu demo nếu parse lỗi
                createFallbackPlayerList();
            }

        });
    }

    /**
     * Parse string response từ server thành List<PlayerData>
     * Format:
     * "username1|displayname1|status1|total1,username2|displayname2|status2|total2,..."
     */
    private List<PlayerData> parsePlayerListFromString(String responseContent) {
        List<PlayerData> playerList = new ArrayList<>();

        if (responseContent == null || responseContent.trim().isEmpty()) {
            return playerList;
        }

        try {
            // Tách các player bằng dấu phẩy
            String[] players = responseContent.split(";");

            for (String playerString : players) {
                // Tách thông tin player bằng dấu |
                String[] playerInfo = playerString.trim().split("\\|");

                if (playerInfo.length >= 4) {
                    String username = playerInfo[0].trim();
                    String displayName = playerInfo[1].trim();
                    String status = playerInfo[2].trim();
                    int totalPoint = Integer.parseInt(playerInfo[3].trim());

                    // Kiểm tra nếu username trùng với user hiện tại thì bỏ qua
                    if (!username.equals(getCurrentUsername())) {
                        PlayerData player = new PlayerData(username,displayName, status, totalPoint);
                        playerList.add(player);
                    }
                } else {
                    System.err.println("Player info không đủ: " + playerString);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi parse player list: " + e.getMessage());
            throw new RuntimeException("Không thể parse danh sách người chơi từ server", e);
        }

        return playerList;
    }

    /**
     * Lấy username của user hiện tại
     */
    private String getCurrentUsername() {
        if (view != null) {
            return view.getUsername();
        }
        return "";
    }

    /**
     * Cập nhật UI với danh sách người chơi mới
     */
    private void updatePlayerListUI(List<PlayerData> playerList) {
        if (homeForm != null) {
            homeForm.updatePlayerList(playerList);
        } else System.out.println("homeform null");
    }

    /**
     * Xử lý lỗi kết nối
     */
    private void handleConnectionError(Exception ex) {
        System.err.println("Lỗi kết nối khi lấy danh sách người chơi: " + ex.getMessage());

        // Fallback: tạo dữ liệu demo khi không kết nối được server
        createFallbackPlayerList();
    }

    /**
     * Gửi lời mời chơi đến server và chờ phản hồi
     */
    public void sendInvite(ModelPlayer player) {
        this.opponentPlayer = player;
        System.out.println("Ðang gửi lời mời đến "+player.getUsername());
        view.showMessageInvite("Đang mời người chơi " + player.getName()+" ...");
        new Thread(() -> {
            try {
//             Gửi yêu cầu mời người chơi
                MessageModel request = new MessageModel("request_invite_player", view.getUsername()+"|"+player.getUsername());
                client.sendMessage(request);

            } catch (Exception ex) {
                SwingUtilities.invokeLater(()
                        -> view.showMessageInvite("Lỗi khi gửi lời mời: " + ex.getMessage())
                );
            }
        }).start();
    }

    public void handleInviteErrorResponse(MessageModel message){
        System.out.println(message.getContent());
    }

    public void handleInviteResponse(MessageModel message){
        SwingUtilities.invokeLater(() -> {
            try{
                String [] parse = message.getContent().split("\\|");
                String inviter = parse[0];
                String invitee = parse[1];
                String inviteeName = parse[2];
                String response = parse[3];
                System.out.println(response);
                switch (response) {
                    case "response_accept":
                        GlassPanePopup.closePopupLast();
                        client.sendMessage(new MessageModel("invite_accept", inviter + "|" + invitee));
                        view.showMessageInvite(inviteeName + " đã chấp nhận. Đang bắt đầu trò chơi..."); // optional
                        break;
                    case "response_reject":
                        GlassPanePopup.closePopupLast();
                        view.showMessageInvite(inviteeName + " đã từ chối lời mời.");
                        break;
                    default:
                        view.showMessageInvite(message.getContent());
                        break;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void handleReceiveInvite(MessageModel message){
        String[] parts = message.getContent().split("\\|");
        String senderUsername = parts[0].split(",")[0];
        String senderDisplayName = parts[0].split(",")[1];
        String receiverUsername = parts[1];
        System.out.println(senderUsername+" "+senderDisplayName+" "+receiverUsername);
        SwingUtilities.invokeLater(() -> {
            view.showMessageInvited(senderUsername,receiverUsername,senderDisplayName);
        });

    }
    /**
     * Tạo danh sách người chơi demo khi không kết nối được server
     */
    private void createFallbackPlayerList() {
        List<PlayerData> fallbackList = new ArrayList<>();

        // Tạo danh sách demo, loại bỏ user hiện tại
        String currentUser = getCurrentUsername();

        if (!"a".equals(currentUser)) {
            fallbackList.add(new PlayerData("Jony A", "Online", 20));
        }
        if (!"b".equals(currentUser)) {
            fallbackList.add(new PlayerData("Jony B", "Busy", 15));
        }
        if (!"c".equals(currentUser)) {
            fallbackList.add(new PlayerData("Jony C", "Offline", 25));
        }

        updatePlayerListUI(fallbackList);
    }

    public void handleGameStart(GameStartDTO dto) {
        SwingUtilities.invokeLater(() -> {
            // Mở GameScreen (nếu chưa mở), truyền tcp client để controller có thể lắng nghe thêm
            if (currentGameScreen == null) {
                currentGameScreen = new GameScreen(client);
                currentGameScreen.setLocationRelativeTo(null);
                currentGameScreen.setVisible(true);
            }
            // Forward DTO để GameScreen bắt đầu countdown và hiển thị batch
            currentGameScreen.startGame(dto);

            // Ẩn dashboard
            if (view != null) view.setVisible(false);
        });
    }

    public void setHomeForm(HomeForm homeForm) {
        this.homeForm = homeForm;
    }

    /**
     * Kiểm tra trạng thái hoạt động
     */
    public boolean isRunning() {
        return isRunning;
    }
}
