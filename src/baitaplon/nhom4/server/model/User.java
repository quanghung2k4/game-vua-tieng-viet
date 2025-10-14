package baitaplon.nhom4.server.model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;        // Lưu chuỗi hash (SHA256 hoặc BCrypt)
    private String displayName;
    private String status;          // online / offline / banned
    
    // Constructor mặc định
    public User() {
        this.status = "offline";
    }

    // Constructor đầy đủ
    public User(int userId, String username, String password, String displayName, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.status = status != null ? status : "offline";
    }
    
    // Constructor không có userId (dùng khi tạo user mới)
    public User(String username, String password, String displayName) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.status = "offline";
    }
    
    // Getters và Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    
    // Phương thức tiện ích
    public boolean isOnline() {
        return "online".equals(this.status);
    }
    
    public boolean isBanned() {
        return "banned".equals(this.status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId == user.userId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}
