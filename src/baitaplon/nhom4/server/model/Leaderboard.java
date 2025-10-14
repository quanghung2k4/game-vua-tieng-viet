package baitaplon.nhom4.server.model;

public class Leaderboard {
    private int userId;
    private int totalPoints;
    private int totalMatches;

    // Constructor mặc định
    public Leaderboard() {
        this.totalPoints = 0;
        this.totalMatches = 0;
    }

    // Constructor đầy đủ
    public Leaderboard(int userId, int totalPoints, int totalMatches) {
        this.userId = userId;
        this.totalPoints = totalPoints;
        this.totalMatches = totalMatches;
    }

    // Constructor chỉ với userId
    public Leaderboard(int userId) {
        this.userId = userId;
        this.totalPoints = 0;
        this.totalMatches = 0;
    }

    // Getters và Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    // Phương thức tiện ích
    public void addPoints(int points) {
        this.totalPoints += points;
    }

    public void incrementMatches() {
        this.totalMatches++;
    }

    public void addMatchResult(boolean isWin, boolean isDraw) {
        incrementMatches();
        if (isWin) {
            addPoints(3); // 3 điểm cho thắng
        } else if (isDraw) {
            addPoints(1); // 1 điểm cho hòa
        }
        // 0 điểm cho thua
    }

    public double getWinRate() {
        if (totalMatches == 0)
            return 0.0;
        // Tính tỷ lệ thắng dựa trên điểm số
        // Giả sử: thắng = 3 điểm, hòa = 1 điểm, thua = 0 điểm
        int maxPossiblePoints = totalMatches * 3;
        return (double) totalPoints / maxPossiblePoints * 100;
    }

    public int getEstimatedWins() {
        // Ước tính số trận thắng (giả sử mỗi trận thắng = 3 điểm)
        return totalPoints / 3;
    }

    public int getEstimatedDraws() {
        // Ước tính số trận hòa
        int remainingPoints = totalPoints % 3;
        return remainingPoints;
    }

    public int getEstimatedLosses() {
        // Ước tính số trận thua
        return totalMatches - getEstimatedWins() - getEstimatedDraws();
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "userId=" + userId +
                ", totalPoints=" + totalPoints +
                ", totalMatches=" + totalMatches +
                ", winRate=" + String.format("%.2f", getWinRate()) + "%" +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Leaderboard that = (Leaderboard) obj;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}
