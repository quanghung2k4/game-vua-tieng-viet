package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.model.PlayerData;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.view.DashBoard;
import baitaplon.nhom4.client.component.HomeForm;
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
    private final HomeForm homeForm;
    private Timer refreshTimer;
    private boolean isRunning = false;
    
    public DashBoardController(DashBoard view, TCPClient client, HomeForm homeForm) {
        this.view = view;
        this.client = client;
        this.homeForm = homeForm;
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
                MessageModel response = (MessageModel) client.sendMessage(request);
                
                // Xử lý response
                handlePlayerListResponse(response);
                
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
    private void handlePlayerListResponse(MessageModel response) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                System.err.println("Không nhận được phản hồi từ server!");
                return;
            }
            
            String responseContent = response.getContent();
            System.out.println("Server response: " + responseContent);
            
            if (response.getType().equals("return_get_players")) {
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
            } else {
                System.err.println("Loại response không mong đợi: " + response.getType());
                // Fallback: tạo dữ liệu demo
                createFallbackPlayerList();
            }
        });
    }
    
    /**
     * Parse string response từ server thành List<PlayerData>
     * Format: "username1|displayname1|status1|total1,username2|displayname2|status2|total2,..."
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
                        PlayerData player = new PlayerData(displayName, status, totalPoint);
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
        }
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
    
    /**
     * Kiểm tra trạng thái hoạt động
     */
    public boolean isRunning() {
        return isRunning;
    }
}
