package baitaplon.nhom4.client.model;

import java.io.Serializable;

/**
 * Model chứa thông tin người chơi từ server
 */
public class PlayerData implements Serializable {
    private String displayName;
    private String status;
    private int totalPoint;
    
    public PlayerData() {
    }
    
    public PlayerData(String displayName, String status, int totalPoint) {
        this.displayName = displayName;
        this.status = status;
        this.totalPoint = totalPoint;
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
