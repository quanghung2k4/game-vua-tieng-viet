package baitaplon.nhom4.server.model;

import java.time.LocalDateTime;

public class Match {
    private int matchId;
    private int user1Id;
    private int user2Id;
    private Integer resultUser1; // 1: thắng, 0: thua, 2: hòa, null: chưa kết thúc
    private Integer resultUser2; // 1: thắng, 0: thua, 2: hòa, null: chưa kết thúc
    private LocalDateTime playedAt;

    // Constructor mặc định
    public Match() {
        this.playedAt = LocalDateTime.now();
    }

    // Constructor đầy đủ
    public Match(int matchId, int user1Id, int user2Id, Integer resultUser1, Integer resultUser2,
            LocalDateTime playedAt) {
        this.matchId = matchId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.resultUser1 = resultUser1;
        this.resultUser2 = resultUser2;
        this.playedAt = playedAt != null ? playedAt : LocalDateTime.now();
    }

    // Constructor không có matchId (dùng khi tạo match mới)
    public Match(int user1Id, int user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.playedAt = LocalDateTime.now();
    }

    // Getters và Setters
    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public Integer getResultUser1() {
        return resultUser1;
    }

    public void setResultUser1(Integer resultUser1) {
        this.resultUser1 = resultUser1;
    }

    public Integer getResultUser2() {
        return resultUser2;
    }

    public void setResultUser2(Integer resultUser2) {
        this.resultUser2 = resultUser2;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    // Phương thức tiện ích
    public boolean isFinished() {
        return resultUser1 != null && resultUser2 != null;
    }

    public boolean isUser1Winner() {
        return resultUser1 != null && resultUser1 == 1;
    }

    public boolean isUser2Winner() {
        return resultUser2 != null && resultUser2 == 1;
    }

    public boolean isDraw() {
        return resultUser1 != null && resultUser2 != null &&
                resultUser1 == 2 && resultUser2 == 2;
    }

    public int getWinnerId() {
        if (isUser1Winner())
            return user1Id;
        if (isUser2Winner())
            return user2Id;
        return -1; // Không có người thắng hoặc hòa
    }

    public int getLoserId() {
        if (isUser1Winner())
            return user2Id;
        if (isUser2Winner())
            return user1Id;
        return -1; // Không có người thua hoặc hòa
    }

    // Phương thức để set kết quả trận đấu
    public void setMatchResult(int winnerId, int loserId) {
        if (winnerId == user1Id && loserId == user2Id) {
            this.resultUser1 = 1; // user1 thắng
            this.resultUser2 = 0; // user2 thua
        } else if (winnerId == user2Id && loserId == user1Id) {
            this.resultUser1 = 0; // user1 thua
            this.resultUser2 = 1; // user2 thắng
        }
    }

    // Phương thức để set kết quả hòa
    public void setDrawResult() {
        this.resultUser1 = 2;
        this.resultUser2 = 2;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", user1Id=" + user1Id +
                ", user2Id=" + user2Id +
                ", resultUser1=" + resultUser1 +
                ", resultUser2=" + resultUser2 +
                ", playedAt=" + playedAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Match match = (Match) obj;
        return matchId == match.matchId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(matchId);
    }
}
