package baitaplon.nhom4.server.service;

import java.sql.*;
import java.time.LocalDateTime;
public class GameResultService {
    private final Connection conn;

    public GameResultService(Connection conn) {
        this.conn = conn;
    }

    public void createGameResult(int user1Id, int user2Id, int result1, int result2, LocalDateTime playedAt) {
        String insertGameSql = "INSERT INTO matches (user1_id, user2_id, result_user1, result_user2, played_at) VALUES (?, ?, ?, ?, ?)";
        System.out.println("ghi dtb");
        try (PreparedStatement ps = conn.prepareStatement(insertGameSql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, result1);
            ps.setInt(4, result2);
            ps.setTimestamp(5, Timestamp.valueOf(playedAt));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Đã tạo bản ghi game result: " + user1Id + " vs " + user2Id);
            } else {
                System.err.println("⚠️ Không thể tạo bản ghi game result!");
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tạo bản ghi game result:");
            e.printStackTrace();
        }
    }

}
