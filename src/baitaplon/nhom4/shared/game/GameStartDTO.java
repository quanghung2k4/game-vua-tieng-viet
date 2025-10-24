package baitaplon.nhom4.shared.game;

import java.io.Serializable;

public class GameStartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String player1;
    private final String player2;
    private final WordBatchDTO batch;
    private final long startAtEpochMs;     // thời điểm bắt đầu chung (server đặt now + 3.5s)
    private final int countdownSeconds;    // fallback hiển thị countdown

    public GameStartDTO(String player1, String player2, WordBatchDTO batch, long startAtEpochMs, int countdownSeconds) {
        this.player1 = player1;
        this.player2 = player2;
        this.batch = batch;
        this.startAtEpochMs = startAtEpochMs;
        this.countdownSeconds = countdownSeconds;
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public WordBatchDTO getBatch() { return batch; }
    public long getStartAtEpochMs() { return startAtEpochMs; }
    public int getCountdownSeconds() { return countdownSeconds; }
}