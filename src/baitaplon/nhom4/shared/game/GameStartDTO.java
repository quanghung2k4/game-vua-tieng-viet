package baitaplon.nhom4.shared.game;

import java.io.Serializable;

public class GameStartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String inviter;
    private final String player1;
    private final String player2;
    private final String player1DisplayName;
    private final String player2DisplayName;
    private final WordBatchDTO batch;
    private final long startAtEpochMs;     // thời điểm bắt đầu chung (server đặt now + 3.5s)
    private final int countdownSeconds;    // fallback hiển thị countdown

    public GameStartDTO(String inviter, String player1, String player2, String player1DisplayName, String player2DisplayName, WordBatchDTO batch, long startAtEpochMs, int countdownSeconds) {
        this.inviter = inviter;
        this.player1 = player1;
        this.player2 = player2;
        this.player1DisplayName = player1DisplayName;
        this.player2DisplayName = player2DisplayName;
        this.batch = batch;
        this.startAtEpochMs = startAtEpochMs;
        this.countdownSeconds = countdownSeconds;
    }

    public String getInviter() { return inviter; }
    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public String getPlayer1DisplayName() { return player1DisplayName; }
    public String getPlayer2DisplayName() { return player2DisplayName; }
    public WordBatchDTO getBatch() { return batch; }
    public long getStartAtEpochMs() { return startAtEpochMs; }
    public int getCountdownSeconds() { return countdownSeconds; }
}