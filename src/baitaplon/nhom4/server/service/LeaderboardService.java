package baitaplon.nhom4.server.service;

import java.sql.*;

public class LeaderboardService {
    private final Connection conn;

    public LeaderboardService(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lấy bảng xếp hạng (leaderboard) sắp xếp theo điểm số giảm dần
     * @return Chuỗi chứa thông tin bảng xếp hạng theo định dạng: displayName|totalPoints|totalMatches|totalWins
     */
    public String getLeaderboard() {
        String sql = "SELECT u.display_name, " +
                    "COALESCE(l.total_points, 0) as total_points, " +
                    "COALESCE(l.total_matches, 0) as total_matches, " +
                    "COALESCE(l.total_wins, 0) as total_wins " +
                    "FROM users u " +
                    "LEFT JOIN leaderboard l ON u.user_id = l.user_id " +
                    "ORDER BY total_points DESC, total_wins DESC, total_matches ASC";
        
        StringBuilder leaderboardData = new StringBuilder();
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    leaderboardData.append(";");
                }
                
                String displayName = rs.getString("display_name");
                int totalPoints = rs.getInt("total_points");
                int totalMatches = rs.getInt("total_matches");
                int totalWins = rs.getInt("total_wins");
                
                // Định dạng: displayName|totalPoints|totalMatches|totalWins
                leaderboardData.append(displayName != null ? displayName : "Unknown")
                        .append("|")
                        .append(totalPoints)
                        .append("|")
                        .append(totalMatches)
                        .append("|")
                        .append(totalWins);
                
                first = false;
            }
            
            return leaderboardData.toString();
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy bảng xếp hạng: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi database khi lấy bảng xếp hạng: " + e.getMessage());
        }
    }

    /**
     * Tạo bản ghi leaderboard mới cho user vừa đăng ký
     * @param userId ID của user vừa được tạo
     */
    public void createLeaderboardEntry(int userId) {
        String insertLeaderboardSql = "INSERT INTO leaderboard (user_id, total_points, total_matches, total_wins) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(insertLeaderboardSql)) {
            ps.setInt(1, userId);
            ps.setInt(2, 0); // Điểm ban đầu = 0
            ps.setInt(3, 0); // Số trận ban đầu = 0
            ps.setInt(4, 0); // Số trận thắng ban đầu = 0
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Đã tạo bản ghi leaderboard cho user ID: " + userId);
            } else {
                System.err.println("⚠️ Không thể tạo bản ghi leaderboard cho user ID: " + userId);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tạo bản ghi leaderboard cho user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật điểm số và thống kê trận đấu cho user
     * @param userId ID của user
     * @param isWin true nếu thắng, false nếu thua
     * @param isDraw true nếu hòa
     */
    public void updateMatchResult(int userId, boolean isWin, boolean isDraw) {
        String updateSql = "UPDATE leaderboard SET " +
                          "total_matches = total_matches + 1, " +
                          "total_points = total_points + ?, " +
                          "total_wins = total_wins + ? " +
                          "WHERE user_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            int pointsToAdd = 0;
            int winsToAdd = 0;
            
            if (isWin) {
                pointsToAdd = 3;
                winsToAdd = 1;
            } else if (isDraw) {
                pointsToAdd = 1;
                winsToAdd = 0;
            }
            
            ps.setInt(1, pointsToAdd);
            ps.setInt(2, winsToAdd);
            ps.setInt(3, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Đã cập nhật kết quả trận đấu cho user ID: " + userId);
            } else {
                System.err.println("⚠️ Không thể cập nhật kết quả trận đấu cho user ID: " + userId);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi cập nhật kết quả trận đấu cho user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lấy thông tin leaderboard của một user cụ thể
     * @param userId ID của user
     * @return Chuỗi chứa thông tin theo định dạng: totalPoints|totalMatches|totalWins
     */
    public String getUserLeaderboardInfo(int userId) {
        String sql = "SELECT total_points, total_matches, total_wins FROM leaderboard WHERE user_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int totalPoints = rs.getInt("total_points");
                int totalMatches = rs.getInt("total_matches");
                int totalWins = rs.getInt("total_wins");
                
                return totalPoints + "|" + totalMatches + "|" + totalWins;
            } else {
                return "0|0|0"; // Nếu chưa có bản ghi leaderboard
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy thông tin leaderboard cho user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return "0|0|0";
        }
    }
}
