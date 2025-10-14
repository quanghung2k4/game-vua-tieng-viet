package baitaplon.nhom4.server.service;

import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.util.PasswordUtils;

import java.sql.*;

public class UserService {
    private final Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    public String checkLogin(String username, String plaintextPassword) {
        String sql = "SELECT password FROM users WHERE username = ?";
        String updateStatusSql = "UPDATE users SET status = 'online' WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Mật khẩu trong DB đã được hash, hash mật khẩu từ client và so sánh
                String hashedInputPassword = PasswordUtils.hashPassword(plaintextPassword);
                if (hashedInputPassword.equals(storedPassword)) {
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateStatusSql)) {
                        psUpdate.setString(1, username);
                        psUpdate.executeUpdate();
                    }
                    return "OK";
                } else {
                    return "SAI_MAT_KHAU";
                }
            } else {
                return "KHONG_TON_TAI";
            }

        } catch (SQLException e) {
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

        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, username);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                return "USERNAME_DA_TON_TAI"; // Username đã tồn tại
            }

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, username);
                insertPs.setString(2, PasswordUtils.hashPassword(password));
                insertPs.setString(3, fullname);
                insertPs.executeUpdate();
                return "OK";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "LOI_DB";
        }
    }


    public String getAllPlayers() {
        String sql = "SELECT u.username, u.display_name, u.status, " +
                    "COALESCE(l.total_points, 0) as total_points " +
                    "FROM users u " +
                    "LEFT JOIN leaderboard l ON u.user_id = l.user_id " +
                    "ORDER BY u.user_id";
        StringBuilder playersData = new StringBuilder();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
                        .append(status != null ? status : "offline")
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
        String sql = "SELECT u.display_name, u.status, " +
                    "COALESCE(l.total_points, 0) as total_points " +
                    "FROM users u " +
                    "LEFT JOIN leaderboard l ON u.user_id = l.user_id " +
                    "WHERE u.status = 'online' " +
                    "ORDER BY u.user_id";
        StringBuilder playersData = new StringBuilder();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

}
