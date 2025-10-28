package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.LeaderboardRow;
import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.component.RankScore;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardController {
    private final RankScore view;
    private final TCPClient client;

    public LeaderboardController(RankScore view, TCPClient client) {
        this.view = view;
        this.client = client;
    }

    public void loadLeaderboard() {
        new Thread(() -> {
            try {
                MessageModel request = new MessageModel("request_leaderboard", "");
                MessageModel response = (MessageModel) client.sendMessage(request);
                // Log nhẹ để kiểm tra phản hồi
                if (response != null) {
                    String preview = response.getContent() != null && response.getContent().length() > 200
                        ? response.getContent().substring(0, 200) + "..." : response.getContent();
                    System.out.println("[Leaderboard] type=" + response.getType() + ", contentPreview=" + preview);
                }
                handleLeaderboardResponse(response);
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> view.clearTable());
            }
        }).start();
    }

    private void handleLeaderboardResponse(MessageModel response) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                view.clearTable();
                return;
            }
            // Chấp nhận cả khi type không khớp tuyệt đối, miễn có nội dung
            if (response.getType() != null && !"return_leaderboard".equals(response.getType())) {
                // vẫn tiếp tục parse, vì server có thể đặt type khác
            }
            String content = response.getContent();
            List<LeaderboardRow> rows = parseRows(content);
            view.setLeaderboard(rows);
        });
    }

    private List<LeaderboardRow> parseRows(String content) {
        List<LeaderboardRow> list = new ArrayList<>();
        if (content == null || content.trim().isEmpty()) return list;
        // Hỗ trợ phân tách theo ; hoặc xuống dòng
        String[] lines = content.split("[;\\n]+");
        for (String line : lines) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 4) {
                String displayName = parts[0].trim();
                int totalPoints = safeInt(parts[1]);
                int totalMatches = safeInt(parts[2]);
                int totalWins = safeInt(parts[3]);
                list.add(new LeaderboardRow(displayName, totalPoints, totalMatches, totalWins));
            }
        }
        return list;
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
}


