package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.service.GameResultService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    // map 1-1 giữa 2 username
    private final Map<String, String> PAIRS = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> START_TIMES = new ConcurrentHashMap<>();
    private Connection conn;
    
    public GameSessionManager(Connection conn) {
        this.conn = conn;
    }

    public void registerPair(Connection conn, String u1, String u2) {
        if (u1 == null || u2 == null) return;
        PAIRS.put(u1, u2);
        PAIRS.put(u2, u1);

        LocalDateTime now = LocalDateTime.now();
        START_TIMES.put(u1, now);
        START_TIMES.put(u2, now);
    }

    public String getOpponent(String user) {
        return user == null ? null : PAIRS.get(user);
    }
    public LocalDateTime getStartTime(String user) {
        return START_TIMES.get(user);
    }

    public void removePair(String u1, String u2) {
        if (u1 != null) {
            PAIRS.remove(u1);
            START_TIMES.remove(u1);
        }
        if (u2 != null) {
            PAIRS.remove(u2);
            START_TIMES.remove(u2);
        }
    }

    public void endGame(String p1, String p2, String pWin, String reason) {
        System.out.println(p1+" "+p2+" "+pWin+" "+reason);
        try {
            ClientHandler player1 = MainServer.getClientHandlerByUserName(p1);
            ClientHandler player2 = MainServer.getClientHandlerByUserName(p2);
            if(pWin == null){
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", "Win" + "|" + reason));
            }
            else{
                String result1="", result2="";
                if(pWin.equals("1")){ result1 = "Win"; result2 = "Lose"; }
                else if (pWin.equals("2")){ result1 = "Lose"; result2 = "Win"; }
                else { result1 = "Draw"; result2 = "Draw"; }
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", result1 + "|" + reason));
                if (player2 != null) player2.sendMessage(new MessageModel("game_end", result2 + "|" + reason));
            }
            recordGameResult(p1, p2, pWin);
            removePair(p1, p2);
        } catch (Exception ignored) {}
    }

    private void recordGameResult(String p1, String p2, String pWin) {
        int user1Id = Integer.parseInt(p1);
        int user2Id = Integer.parseInt(p2);
        int result1 = 0, result2 = 0;
        if(pWin == null || pWin.equals("1")){
            result1 =  1;
            result2 = -1;
        } else if(pWin.equals("2")){
            result1 = -1;
            result2 = 1;
        }
        LocalDateTime playedAt = getStartTime(p1);
        GameResultService gameResultService = new GameResultService(conn);
        if(user1Id < user2Id) gameResultService.createGameResult(user1Id, user2Id, result1, result2, playedAt);
        else gameResultService.createGameResult(user2Id, user1Id, result2, result1, playedAt);
    }

    public void finishGame(String p1, String p2, String pWin, String reason) {
        endGame(p1, p2, pWin, reason);
    }

    public void playerOut(String loser) {
        String opp = getOpponent(loser);
        if (opp != null) {
            endGame(opp, loser, null, "disconnect");
        }
    }

    public void notifyDisconnect(String user) {
        String opp = getOpponent(user);
        if (opp != null) {
            endGame(opp, user, null, "disconnect");
        }
    }
}