package baitaplon.nhom4.client.model;

public class LeaderboardRow {
    private final String displayName;
    private final int totalPoints;
    private final int totalMatches;
    private final int totalWins;

    public LeaderboardRow(String displayName, int totalPoints, int totalMatches, int totalWins) {
        this.displayName = displayName;
        this.totalPoints = totalPoints;
        this.totalMatches = totalMatches;
        this.totalWins = totalWins;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getTotalWins() {
        return totalWins;
    }
}



