package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.service.GameResultService;
import baitaplon.nhom4.server.service.LeaderboardService;
import baitaplon.nhom4.server.service.UserService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    // map 1-1 giữa 2 username
    private static final Map<String, String> PAIRS = new ConcurrentHashMap<>();
    private static final Map<String, LocalDateTime> START_TIMES = new ConcurrentHashMap<>();
    private static Connection conn;

    public static void init(Connection connection) {
        conn = connection;
    }

    public static void registerPair(String u1, String u2) {
        if (u1 == null || u2 == null) return;
        PAIRS.put(u1, u2);
        PAIRS.put(u2, u1);

        LocalDateTime now = LocalDateTime.now();
        START_TIMES.put(u1, now);
        START_TIMES.put(u2, now);
    }

    public static String getOpponent(String user) {
        return user == null ? null : PAIRS.get(user);
    }
    public static LocalDateTime getStartTime(String user) {
        return START_TIMES.get(user);
    }

    public static void removePair(String u1, String u2) {
        if (u1 != null) {
            PAIRS.remove(u1);
            START_TIMES.remove(u1);
        }
        if (u2 != null) {
            PAIRS.remove(u2);
            START_TIMES.remove(u2);
        }
    }

    public static void endGame(String p1, String p2, String pWin, String reason) {
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

    private static void recordGameResult(String p1, String p2, String pWin) {
        System.out.println("p1 = " + p1 + ", p2 = " + p2);
        UserService userService = new UserService(conn);
        User user1 = userService.getUserByUserName(p1);
        User user2 = userService.getUserByUserName(p2);

        int user1Id = user1.getUserId();
        int user2Id = user2.getUserId();
        int result1 = 1, result2 = 1;
        if(pWin == null || pWin.equals("1")){
            result1 =  3;
            result2 = 0;
        } else if(pWin.equals("2")){
            result1 = 0;
            result2 = 3;
        }
        System.out.println("Lấy thời gian bắt đầu cho p1: " + p1);
        LocalDateTime playedAt = getStartTime(p1);
        System.out.println("playedAt = " + playedAt);
        GameResultService gameResultService = new GameResultService(conn);
        LeaderboardService leaderBoardService = new LeaderboardService(conn);

        if(user1Id < user2Id) gameResultService.createGameResult(user1Id, user2Id, result1, result2, playedAt);
        else gameResultService.createGameResult(user2Id, user1Id, result2, result1, playedAt);

        leaderBoardService.updateMatchResult(user1Id, result1 == 3, result1 == 1);
        leaderBoardService.updateMatchResult(user2Id, result2 == 3, result2 == 1);
    }

    public static void finishGame(String p1, String p2, String pWin, String reason) {
        endGame(p1, p2, pWin, reason);
    }

    public static void playerOut(String loser) {
        String opp = getOpponent(loser);
        if (opp != null) {
            endGame(opp, loser, null, "disconnect");
        }
    }

    public static void notifyDisconnect(String user) {
        String opp = getOpponent(user);
        if (opp != null) {
            endGame(opp, user, null, "disconnect");
        }
    }
}