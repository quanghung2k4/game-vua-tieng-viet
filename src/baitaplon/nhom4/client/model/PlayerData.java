package baitaplon.nhom4.client.model;

import java.io.Serializable;

/**
 * Model chứa thông tin người chơi từ server
 */
public class PlayerData implements Serializable {
    private int id;
    private String username;
    private String displayName;
    private String status;
    private int totalPoint;
    
    public PlayerData() {
    }

     public PlayerData(String username,String displayName, String status, int totalPoint) {
        this.username = username;
        this.displayName = displayName;
        this.status = status;
        this.totalPoint = totalPoint;
    }

    
    public PlayerData(int id,String displayName, String status, int totalPoint) {
        this.id = id;
        this.displayName = displayName;
        this.status = status;
        this.totalPoint = totalPoint;
    }

    
    
    public PlayerData(String displayName, String status, int totalPoint) {
        this.displayName = displayName;
        this.status = status;
        this.totalPoint = totalPoint;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    public int getTotalPoint() {
        return totalPoint;
    }
    
    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }
    
    @Override
    public String toString() {
        return "PlayerData{" +
                "displayName='" + displayName + '\'' +
                ", status='" + status + '\'' +
                ", totalPoint=" + totalPoint +
                '}';
    }
}
