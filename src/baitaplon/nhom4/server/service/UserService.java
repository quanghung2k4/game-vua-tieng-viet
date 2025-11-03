package baitaplon.nhom4.server.service;

import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.util.PasswordUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserService {

    private final Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    public String checkLogin(String username, String plaintextPassword) {
        String sql = "SELECT password FROM users WHERE username = ?";
        String updateStatusSql = "UPDATE users SET status = 'Online' WHERE username = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Mật khẩu trong DB đã được hash, hash mật khẩu từ client và so sánh
                String hashedInputPassword = PasswordUtils.hashPassword(plaintextPassword);
                if (hashedInputPassword.equals(storedPassword)) {
                    try ( PreparedStatement psUpdate = conn.prepareStatement(updateStatusSql)) {
                        psUpdate.setString(1, username);
                        psUpdate.executeUpdate();
                    }
                    return "OK";
                } else {
                    return "SAI_MAT_KHAU";
                }
            } else {
                return "USERNAME KHONG TON TAI";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "LOI_DB";
        }
    }

    public String logout(String username) {
        String updateStatusSql = "UPDATE users SET status = 'Offline' WHERE username = ?";

        try ( PreparedStatement ps = conn.prepareStatement(updateStatusSql)) {
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ User " + username + " đã logout thành công");
                return "OK";
            } else {
                System.err.println("⚠️ Không tìm thấy user " + username + " để logout");
                return "USER NOT FOUND";
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi logout user " + username + ": " + e.getMessage());
            e.printStackTrace();
            return "LOI_DB";
        }
    }

    public String registerUser(User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        String fullname = user.getDisplayName();
        // Kiểm tra username có tồn tại chưa
        String checkSql = "SELECT username FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, display_name) VALUES (?, ?, ?)";

        try ( PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, username);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                return "USERNAME DA TON TAI"; // Username đã tồn tại
            }

            try ( PreparedStatement insertPs = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertPs.setString(1, username);
                insertPs.setString(2, PasswordUtils.hashPassword(password));
                insertPs.setString(3, fullname);
                int affectedRows = insertPs.executeUpdate();

                if (affectedRows > 0) {
                    // Lấy user_id vừa được tạo
                    ResultSet generatedKeys = insertPs.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        // Tạo bản ghi leaderboard cho user mới
                        createLeaderboardEntry(userId);
                    }
                }
                return "OK";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "LOI_DB";
        }
    }

    public String getAllPlayers() {
        String sql = "SELECT u.username, u.display_name, u.status, "
                + "COALESCE(l.total_points, 0) as total_points "
                + "FROM users u "
                + "LEFT JOIN leaderboard l ON u.user_id = l.user_id "
                + "ORDER BY u.user_id";
        StringBuilder playersData = new StringBuilder();

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    playersData.append(";");
                }
                String username = rs.getString("username");
                String displayName = rs.getString("display_name");
                String status = rs.getString("status");
                int totalPoints = rs.getInt("total_points");

                // Định dạng: displayName|status|totalPoints
                playersData.append(username)
                        .append("|")
                        .append(displayName != null ? displayName : "")
                        .append("|")
                        .append(status != null ? status : "Offline")
                        .append("|")
                        .append(totalPoints);

                first = false;
            }

            return playersData.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi database khi lấy danh sách người chơi: " + e.getMessage());
        }
    }

    public String getOnlinePlayers() {
        String sql = "SELECT u.display_name, u.status, "
                + "COALESCE(l.total_points, 0) as total_points "
                + "FROM users u "
                + "LEFT JOIN leaderboard l ON u.user_id = l.user_id "
                + "WHERE u.status = 'online' "
                + "ORDER BY u.user_id";
        StringBuilder playersData = new StringBuilder();

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    playersData.append(";");
                }

                String displayName = rs.getString("display_name");
                String status = rs.getString("status");
                int totalPoints = rs.getInt("total_points");

                // Định dạng: username|displayName|status|totalPoints
                playersData.append("|")
                        .append(displayName != null ? displayName : "")
                        .append("|")
                        .append(status)
                        .append("|")
                        .append(totalPoints);

                first = false;
            }

            return playersData.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi database khi lấy danh sách người chơi online: " + e.getMessage());
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, display_name, status FROM users WHERE user_id = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setDisplayName(rs.getString("display_name"));
                user.setStatus(rs.getString("status"));
                return user;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi database khi lấy thông tin người chơi: " + e.getMessage());
        }
    }

    public User getUserByUserName(String username) {
        String sql = "SELECT user_id, username, display_name, status FROM users WHERE username = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setDisplayName(rs.getString("display_name"));
                user.setStatus(rs.getString("status"));
                return user;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi database khi lấy thông tin người chơi: " + e.getMessage());
        }
    }

    public String getHistoryByUserName(String username) {
        String sql = "SELECT u.display_name, m.result_user1, m.result_user2, m.played_at "
                + "FROM matches m "
                + "JOIN users u on m.user2_id = u.user_id "
                + "WHERE m.user1_id = (SELECT u.user_id FROM users u WHERE u.username = ?) ";

        StringBuilder playersData = new StringBuilder();

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
                
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    playersData.append(";");
                }
                
                String displayName = rs.getString("display_name");
                int result1 = rs.getInt("result_user1");
                int result2 = rs.getInt("result_user2");
                String result = "Hòa";
                if(result1 > result2) result = "Thắng";
                Timestamp time = rs.getTimestamp("played_at");
                
                LocalDateTime time_vn = time.toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                String time_format = time_vn.format(formatter);
                // Định dạng: displayName|timeStart|result
                playersData.append("|")
                        .append(displayName != null ? displayName : "")
                        .append("|")
                        .append(time_format)
                        .append("|")
                        .append(result1 < result2 ? "Thua": result);
                first = false;
            }

        } catch (Exception e) {

        }
        System.out.println("parse: "+playersData.toString());
        return playersData.toString();
    }

    private void createLeaderboardEntry(int userId) {
        String insertLeaderboardSql = "INSERT INTO leaderboard (user_id, total_points, total_matches, total_wins) VALUES (?, ?, ?, ?)";

        try ( PreparedStatement ps = conn.prepareStatement(insertLeaderboardSql)) {
            ps.setInt(1, userId);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);

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

}
